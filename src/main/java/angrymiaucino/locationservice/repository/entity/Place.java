package angrymiaucino.locationservice.repository.entity;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("place")
public class Place {

    @Id
    private Long id;

    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private Point location;  // This will hold the JTS Point object


    public Place() {}

    public Place(String name, String description, Double latitude, Double longitude, Point location) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    // Helper methods to convert latitude and longitude to Point
    public void updateLocationFromCoordinates() {
        if (latitude != null && longitude != null) {
            this.location = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
        }
    }

    // Helper method to update latitude and longitude from Point
    public void updateCoordinatesFromLocation() {
        if (location != null) {
            this.latitude = location.getY();  // Latitude is the Y coordinate
            this.longitude = location.getX(); // Longitude is the X coordinate
        }
    }
}
