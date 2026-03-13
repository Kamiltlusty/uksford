package pl.uksford.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uksford.api.entity.Vote;
import pl.uksford.api.entity.VoteId;

public interface VoteRepository extends JpaRepository<Vote, VoteId> {
}