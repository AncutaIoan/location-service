package angrymiaucino.locationservice.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // This annotation will be applied to methods only
@Retention(RetentionPolicy.RUNTIME)  // The annotation will be available at runtime
public @interface RunSql {
    String[] scripts();  // Array of scripts to run
}
