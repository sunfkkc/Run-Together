package capstone.Runtogether.service;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.domain.Role;
import capstone.Runtogether.dto.JwtRequestDto;
import capstone.Runtogether.dto.MemberDto;
import capstone.Runtogether.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class MemberService implements UserDetailsService {



    private MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();

    }
    /**회원가입*/
    public String join(MemberDto memberDto){

        //중복 계정 검증
        boolean existEmail = memberRepository.existsById(memberDto.getEmail());
        if (existEmail) return "existEmail";

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

    /**로그인*/
    public String login(JwtRequestDto jwtRequestDto) throws Exception{
        boolean matches = passwordEncoder.matches(jwtRequestDto.getPwd(), loadUserByUsername(jwtRequestDto.getEmail()).getPassword());
        if (matches){

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequestDto.getEmail(), jwtRequestDto.getPwd()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member principal = (Member) authentication.getPrincipal();
            return principal.getUsername();
        }
        else throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Member member =memberRepository.findById(userEmail)
                .orElseThrow(()-> new UsernameNotFoundException("등록되지 않은 사용자 입니다"));

        return member;
    }

    public Optional<Member> findOne(String memberEmail){
        return memberRepository.findById(memberEmail);
    }

}
