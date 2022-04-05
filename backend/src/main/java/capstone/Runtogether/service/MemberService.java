package capstone.Runtogether.service;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.dto.MemberDto;
import capstone.Runtogether.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService implements UserDetailsService {



    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();

    }

    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }
     /**회원가입*/
    public String join(MemberDto memberDto){

        if (existEmail(memberDto)) return "existEmail"; //계정 중복 검증

        if(existName(memberDto)) return "existName"; // 닉네임 중복 검증

        memberDto.setPwd(passwordEncoder.encode(memberDto.getPwd()));
        Member member = new Member(memberDto);
        memberRepository.save(member);
        return member.getEmail();
    }

    private boolean existName(MemberDto member){
        return memberRepository.findByName(member.getName())
                .isPresent();
    }

    private boolean existEmail(MemberDto member){
        return memberRepository.findByEmail(member.getEmail())
                .isPresent();
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member findMember =memberRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("등록되지 않은 사용자 입니다"));

        if (findMember != null){
            return findMember;
        }
        return null;
    }
    public Optional<Member> findOne(String memberEmail){
        return memberRepository.findById(memberEmail);
    }



}
