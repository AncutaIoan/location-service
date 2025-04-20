package angrymiaucino.locationservice.config;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Optional;

public class RunSqlExtension implements BeforeTestExecutionCallback {

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        extensionContext.getTestMethod()
                .flatMap(method -> Optional.ofNullable(method.getAnnotation(RunSql.class)))
                .ifPresent(annotation -> {
                    Object testInstance = extensionContext.getTestInstance()
                            .orElseThrow(() -> new RuntimeException("Test instance not found. RunSqlExtension is supposed to be used in JUnit 5 only!"));

                    ConnectionFactory connectionFactory = getConnectionFactory(testInstance);
                    if (connectionFactory != null) {
                        for (String script : annotation.scripts()) {
                            Mono.from(connectionFactory.create())
                                    .flatMap(connection -> Mono.from(ScriptUtils.executeSqlScript(connection, new ClassPathResource(script))))
                                    .block();
                        }
                    }
                });
    }

    private ConnectionFactory getConnectionFactory(Object testInstance) {
        try {
            Field field = ReflectionUtils.findField(testInstance.getClass(), "connectionFactory");
            if (field != null) {
                field.setAccessible(true);
                return (ConnectionFactory) field.get(testInstance);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access connectionFactory field.", e);
        }
        return null;
    }
}

