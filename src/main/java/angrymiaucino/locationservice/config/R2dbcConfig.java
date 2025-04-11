package angrymiaucino.locationservice.config;

import angrymiaucino.locationservice.repository.converter.PointToPGGeometryConverter;
import angrymiaucino.locationservice.repository.converter.StringToPointConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.support.GenericConversionService;


@Configuration
public class R2dbcConfig {

    @Primary
    @Bean
    public GenericConversionService dbConversionService() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new PointToPGGeometryConverter()); // Register your existing converter
        conversionService.addConverter(new StringToPointConverter());     // Register the String -> Point converter
        return conversionService;
    }
}
