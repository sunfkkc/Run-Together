package capstone.Runtogether.service;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository=memberRepository;
    }

    /**
     * 회원가입
     */

    public Long join(Member member){

        duplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void duplicateMember(Member member){
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
