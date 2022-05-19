package capstone.Runtogether.repository;

import capstone.Runtogether.entity.Board;
import capstone.Runtogether.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    Challenge save(Challenge challenge);
    List<Challenge> findAll();
    List<Challenge> findByState(String state);
    Optional<Challenge> findById(Long challengeId);
    void deleteById(Long challengeId);
}
