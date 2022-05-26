package capstone.Runtogether.repository;

import capstone.Runtogether.entity.Member;
import capstone.Runtogether.entity.Running;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RunningRepository extends JpaRepository<Running, Long> {
    Running save (Running running);

    List<Running> findAllByMemberIdOrderByStartTimeDesc(long memberId);

    Optional<Running> findByRunningId(long runningId);

    Running findOneByMemberIdOrderByStartTimeDesc(long memberId);

    Long deleteByRunningId(long runningId);
}
