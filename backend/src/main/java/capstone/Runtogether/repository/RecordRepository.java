package capstone.Runtogether.repository;

import capstone.Runtogether.entity.Record;
import capstone.Runtogether.entity.Running;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Record save(Record record);
    List<Record> findAllByRunningId(long runningId);
}
