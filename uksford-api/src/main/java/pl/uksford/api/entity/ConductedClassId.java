package pl.uksford.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ConductedClassId implements Serializable {

    @NotNull
    @Column(name = "review_id", nullable = false)
    private Integer reviewId;

    @NotNull
    @Column(name = "instructor_id", nullable = false)
    private Short instructorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConductedClassId entity = (ConductedClassId) o;
        return Objects.equals(this.reviewId, entity.reviewId) &&
                Objects.equals(this.instructorId, entity.instructorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, instructorId);
    }

}