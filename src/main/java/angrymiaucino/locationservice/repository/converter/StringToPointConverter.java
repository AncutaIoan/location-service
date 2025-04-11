package angrymiaucino.locationservice.repository.converter;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.core.convert.converter.Converter;

public class StringToPointConverter implements Converter<String, Point> {

    @Override
    public Point convert(String source) {
        if (source.isEmpty()) {
            return null;  // Handle null or empty string case
        }

        try {
            // Use WKTReader to parse the string into a Point
            WKTReader reader = new WKTReader(new GeometryFactory());
            return (Point) reader.read(source); // Convert the WKT to a Point
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid WKT format: " + source, e);
        }
    }
}