package pl.uksford.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "votes")
public class Vote {

    @EmbeddedId
    private VoteId id = new VoteId();

    // @OnDelete(action = OnDeleteAction.CASCADE) this might cause problems, db can handle it on its own
    @MapsId("reviewId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "is_useful", nullable = false)
    private Boolean isUseful = false;

}