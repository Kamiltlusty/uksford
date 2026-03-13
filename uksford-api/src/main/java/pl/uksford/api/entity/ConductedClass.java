package pl.uksford.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "conducted_classes")
public class ConductedClass {

    @EmbeddedId
    private ConductedClassId id = new ConductedClassId();

    @ManyToOne
    @MapsId("reviewId")
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @MapsId("instructorId")
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
    // no idea what jpa buddy wanted here when generated this entity
    //TODO [Reverse Engineering] generate columns from DB
}