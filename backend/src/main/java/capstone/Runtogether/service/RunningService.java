package capstone.Runtogether.service;

import capstone.Runtogether.dto.RunningDto;
import capstone.Runtogether.entity.Running;
import capstone.Runtogether.repository.RunningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RunningService {
    private final RunningRepository runningRepository;

    @Autowired
    public RunningService(RunningRepository runningRepository) {
        this.runningRepository = runningRepository;
    }

    //러닝 데이터 저장
    public Running completeRunning(RunningDto runningDto, Long memberId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Running running = Running.builder()
                    .memberId(memberId)
                    .distance(runningDto.getDistance())
                    .time(runningDto.getTime())
                    .speed(runningDto.getSpeed())
                    .startTime(now.minusSeconds(runningDto.getTime()))
                    .endTime(now)
                    .polyline(runningDto.getPolyline())
                    .build();

            return runningRepository.save(running);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //러닝 기록 조회
    public List<Running> findAllRunning(Long memberId) {
        return runningRepository.findAllByMemberIdOrderByStartTimeDesc(memberId);

    }
}
