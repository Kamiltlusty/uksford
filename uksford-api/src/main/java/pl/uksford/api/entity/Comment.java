package pl.uksford.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Integer id;

    @Size(max = 800)
    @NotNull
    @Column(name = "content", nullable = false, length = 800)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false) // TODO should it really be nullable? When user get banned/removed we don't want to remove its' comments, votes or reviews, therefore should
    private User user;

    // @OnDelete(action = OnDeleteAction.CASCADE) might cause problems with db. Same as with votes and review
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @NotNull
    @Column(name = "posted_at", nullable = false)
    private Instant postedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 800)
    @Column(name = "original_content", length = 800)
    private String originalContent;

    // TODO not sure if this is needed
    @OneToMany(mappedBy = "parentComment")
    private Set<Comment> comments = new LinkedHashSet<>();

}