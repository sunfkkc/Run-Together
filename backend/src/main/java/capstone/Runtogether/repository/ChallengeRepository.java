package capstone.Runtogether.repository;

import capstone.Runtogether.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    Challenge save(Challenge challenge);
    List<Challenge> findByState(String state);
    void deleteById(Long challengeId);
}
