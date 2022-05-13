package capstone.Runtogether.service;


import capstone.Runtogether.entity.Running;
import capstone.Runtogether.repository.MemberRepository;
import capstone.Runtogether.repository.RunningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecordService {
    private final MemberRepository memberRepository;
    private final RunningRepository runningRepository;

    @Autowired
    public RecordService(MemberRepository memberRepository, RunningRepository runningRepository) {
        this.memberRepository = memberRepository;
        this.runningRepository = runningRepository;
    }

    public List<Running> findRunningByMemberId(long memberId) {
        List<Running> find_running = runningRepository.findAllByMemberIdOrderByStartTimeDesc(memberId);
        return find_running;
    }


}
