package pl.uksford.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import pl.uksford.api.converter.AcademicDegreeConverter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "instructors")
public class Instructor {

    public Instructor(String lastName, String firstName, AcademicDegree academicDegree) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.academicDegree = academicDegree;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id", nullable = false)
    private Short id;

    @Size(max = 30)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Size(max = 30)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @OneToMany(mappedBy = "instructor")
    private Set<ConductedClass> conductedClasses = new LinkedHashSet<>();

    @Column(name = "academic_degree", nullable = false, columnDefinition = "academic_degree")
    @Convert(converter = AcademicDegreeConverter.class)
    @ColumnTransformer(write = "?::academic_degree")
    private AcademicDegree academicDegree;
}