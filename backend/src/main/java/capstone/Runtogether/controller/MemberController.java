package capstone.Runtogether.controller;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.dto.LoginFormDto;
import capstone.Runtogether.dto.MemberDto;
import capstone.Runtogether.service.MemberService;
import capstone.Runtogether.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController extends ApiBaseController{

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private Object status;



    @PostMapping("join")
    public ResponseEntity<String> join(@RequestBody MemberDto memberDto){
        String result = memberService.join(memberDto);
        if (result == "existEmail"){
            return new ResponseEntity<String>("이미 존재하는 계정입니다.", HttpStatus.OK);
        }
        else if ( result == "existName"){
            return new ResponseEntity<String>("이미 존재하는 이름입니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String>("회원가입에 성공", HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginFormDto form, HttpServletResponse response) {
        UserDetails loginMember = memberService.loadUserByUsername(form.getEmail());

        if (loginMember == null){
            return new ResponseEntity<String>("회원이 존재하지 않습니다",HttpStatus.FORBIDDEN);
        }
        if(!passwordEncoder.matches(form.getPwd(), loginMember.getPassword())) {
            return new ResponseEntity<String>("비밀번호가 올바르지 않습니다",HttpStatus.FORBIDDEN);
        }

        String token = jwtTokenProvider.generateAccessToken((Member) loginMember);
        response.setHeader("auth", token);
//        Cookie tokenCookie = new Cookie("auth", token);
//        response.addCookie(tokenCookie);

        return new ResponseEntity<String>("로그인 성공",HttpStatus.OK);
    }
}
