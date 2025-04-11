package angrymiaucino.locationservice.repository.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.locationtech.jts.geom.Point;


@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;

    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;
    // Constructors
    public Place() {}

    public Place(String name, String description, Double latitude, Double longitude, Point location) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now();
        this.location = location;
    }

    // Getters & setters omitted for brevity
}

