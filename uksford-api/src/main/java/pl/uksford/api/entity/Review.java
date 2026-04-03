package pl.uksford.api.entity;

import jakarta.persistence.*;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @Column(name = "engagement", nullable = false)
    private Short engagement;

    @NotNull
    @Column(name = "hardness", nullable = false)
    private Short hardness;

    @NotNull
    @Column(name = "posted_at", nullable = false)
    private Instant postedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Size(max = 1000)
    @Column(name = "content", length = 1000)
    private String content;

    @Size(max = 1000)
    @Column(name = "original_content", length = 1000)
    private String originalContent;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "review")
    private Set<Comment> comments = new LinkedHashSet<>();

    @Size(min = 1)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConductedClass> conductedClasses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "review")
    private Set<Vote> votes = new LinkedHashSet<>();

    public void addInstructor(Instructor instructor) {
        ConductedClass conductedClass = new ConductedClass();
        conductedClass.setInstructor(instructor);
        conductedClass.setReview(this);
        this.conductedClasses.add(conductedClass);
    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }


    // Methods checks if there is at least one instructor assigned to a course.
    // Throws: ConstraintViolationException - if the constraints didn't match
    //         ValidationException          - if buildDefaultValidatorFactory() failed
    @PrePersist
    @PreUpdate
    private void validate() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Review>> violations = validator.validate(this);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder("Id:" + this.getId() +
                "\nEngagement: " + this.getEngagement() +
                "\nHardness: " + this.getHardness() +
                "\nPostAt: " + this.getPostedAt() +
                "\nPoster: " + this.getUser().getNick() +
                "\nCourse code: " + this.getCourse().getCode() +
                "\nContent: : " + this.getContent() +
                "\nInstructor(s): : ");

        for (var conductedClass : this.getConductedClasses()) {
            response.append(conductedClass.getInstructor()).append("\n");
        }

        response.append("Votes: ");
        for (Vote vote : this.getVotes()) {
            response.append(vote.getId()).append("\n");
        }

        response.append("Comms: ");
        for (Comment comment : this.getComments()) {
            response.append(comment.getContent());
        }

        return response.toString();
    }
}