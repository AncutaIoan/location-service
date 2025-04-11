package angrymiaucino.locationservice.repository.converter;

import org.springframework.core.convert.converter.Converter;
import org.locationtech.jts.geom.Point;
import net.postgis.jdbc.PGgeometry;

import java.sql.SQLException;

public class PointToPGGeometryConverter implements Converter<Point, PGgeometry> {

    @Override
    public PGgeometry convert(Point point) {
        if (point == null) {
            return null;
        }
        String wkt = "POINT(" + point.getX() + " " + point.getY() + ")";

        try {
            return new PGgeometry(wkt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}