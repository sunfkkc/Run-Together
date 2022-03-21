package capstone.Runtogether.controller;

import capstone.Runtogether.dto.MemberDTO;
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



    @PostMapping("/member/Join")
    public ResponseEntity<String> create(@RequestBody MemberDTO memberDto){
        memberService.join(memberDto);
        return new ResponseEntity<String>("회원가입에 성공", HttpStatus.OK);
    }
}
