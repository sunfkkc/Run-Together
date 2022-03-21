package capstone.Runtogether.service;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.dto.MemberDTO;
import capstone.Runtogether.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class MemberService {



    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입
     */

    public Member join(MemberDTO memberDTO){
        duplicateEmail(memberDTO); // 회원 회원 검증
        duplicateName(memberDTO); // 닉네임 중복 검증
        Member member = Member.builder()
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .pw(passwordEncoder.encode(memberDTO.getPw()))
                .gender(memberDTO.getGender())
                .build();
        return memberRepository.save(member);
    }

    private void duplicateEmail(MemberDTO member){
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    private void duplicateName(MemberDTO member){
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 닉네임입니다.");
                });
    }



    public List<Member> findMembers(){
        return memberRepository.findAll();

    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
