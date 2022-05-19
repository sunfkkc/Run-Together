package capstone.Runtogether.service;

import capstone.Runtogether.entity.Member;
import capstone.Runtogether.dto.MemberDto;
import capstone.Runtogether.entity.Role;
import capstone.Runtogether.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();

    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /**
     * 회원가입
     */
    public String join(MemberDto memberDto) {

        if (existEmail(memberDto)) return "existEmail"; //계정 중복 검증

        if (existName(memberDto)) return "existName"; // 닉네임 중복 검증

        memberDto.setPwd(passwordEncoder.encode(memberDto.getPwd()));
        Member member = new Member(memberDto);
        if (member.getName().equals("관리자")) member.setRole(Role.ROLE_ADMIN);

        memberRepository.save(member);
        return member.getEmail();
    }


    public ResponseEntity<?> checkEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            return new ResponseEntity<String>("해당 유저가 이미 존재합니다.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>("사용할 수 있는 이메일입니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> checkName(String name) {
        if (memberRepository.findByName(name).isPresent()) {
            return new ResponseEntity<String>("해당 닉네임은 이미 존재합니다.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>("사용할 수 있는 이름입니다.", HttpStatus.OK);
    }

    private boolean existName(MemberDto member) {
        return memberRepository.findByName(member.getName())
                .isPresent();
    }

    private boolean existEmail(MemberDto member) {
        return memberRepository.findByEmail(member.getEmail())
                .isPresent();
    }

    public Optional<Member> findOne(String memberEmail) {
        return memberRepository.findById(memberEmail);
    }


}
