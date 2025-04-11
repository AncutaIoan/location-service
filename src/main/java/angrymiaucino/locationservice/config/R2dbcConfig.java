//package angrymiaucino.locationservice.config;
//
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Point;
//import org.locationtech.jts.io.WKTReader;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.data.convert.ReadingConverter;
//import org.springframework.data.convert.WritingConverter;
//import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
//import org.springframework.data.r2dbc.dialect.PostgresDialect;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class R2dbcConfig {
//
//
//    @Bean
//    public R2dbcCustomConversions customConversions() {
//        List<Converter<?, ?>> converters = new ArrayList<>();
//        converters.add(new StringToPointConverter());
//        converters.add(new PointToStringConverter());
//        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
//    }
//
//    @ReadingConverter
//    public static class StringToPointConverter implements Converter<String, Point> {
//        @Override
//        public Point convert(String source) {
//            try {
//                WKTReader reader = new WKTReader(new GeometryFactory());
//                return (Point) reader.read(source);  // Parse WKT string into Point
//            } catch (Exception e) {
//                throw new IllegalArgumentException("Invalid WKT string for Point: " + source, e);
//            }
//        }
//    }
//
//    @WritingConverter
//    public static class PointToStringConverter implements Converter<Point, String> {
//        @Override
//        public String convert(Point point) {
//            return point.toString();  // Return string representation of Point (WKT)
//        }
//    }
//}