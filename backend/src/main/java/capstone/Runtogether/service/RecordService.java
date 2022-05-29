package capstone.Runtogether.service;


import capstone.Runtogether.dto.RecordDto;
import capstone.Runtogether.dto.RecordForm;
import capstone.Runtogether.entity.Record;
import capstone.Runtogether.entity.Running;
import capstone.Runtogether.repository.MemberRepository;
import capstone.Runtogether.repository.RecordRepository;
import capstone.Runtogether.repository.RunningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecordService {
    private final RunningRepository runningRepository;
    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RunningRepository runningRepository, RecordRepository recordRepository) {
        this.runningRepository = runningRepository;
        this.recordRepository = recordRepository;
    }

    public Long saveRecord(RecordForm form, Long runningId) {
        try {
            if (!form.getRecordDtoList().isEmpty()) {
                for (RecordDto recordDto : form.getRecordDtoList()) {
                    Record record = Record.builder()
                            .runningId(runningId)
                            .distance(recordDto.getDistance())
                            .speed(recordDto.getSpeed())
                            .build();

                    recordRepository.save(record);
                }
            }
            return runningId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //러닝 기록 조회
    public List<Record> findAllRecord(Long runningId) {
        return recordRepository.findAllByRunningId(runningId);

    }

}
