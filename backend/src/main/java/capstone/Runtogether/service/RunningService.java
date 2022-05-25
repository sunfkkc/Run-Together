package capstone.Runtogether.service;

import capstone.Runtogether.dto.RunningDto;
import capstone.Runtogether.entity.Running;
import capstone.Runtogether.repository.RunningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional
public class RunningService {
    private final RunningRepository runningRepository;

    @Autowired
    public RunningService(RunningRepository runningRepository) {
        this.runningRepository = runningRepository;
    }

    public Running completeRunning(RunningDto runningDto, Long memberId) {
        try{
            Running running = Running.builder()
                    .memberId(memberId)
                    .distance(runningDto.getDistance())
                    .time(runningDto.getTime())
                    .speed(runningDto.getSpeed())
                    .build();

            return runningRepository.save(running);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
