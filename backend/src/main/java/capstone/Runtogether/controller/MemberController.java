package capstone.Runtogether.controller;

import capstone.Runtogether.dto.JwtRequestDto;
import capstone.Runtogether.dto.MemberDto;
import capstone.Runtogether.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController extends ApiBaseController{

    private final MemberService memberService;
    private Object status;



    @PostMapping("member/join")
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

    @PostMapping("member/login")
    public ResponseEntity<String> login(@RequestBody JwtRequestDto jwtRequestDto){
        try{
            memberService.login(jwtRequestDto);
            return new ResponseEntity<String>("로그인 성공", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);

        }
    }

}
