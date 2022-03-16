package capstone.Runtogether.controller;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MemberController {

    private final MemberService memberService;
    private Object status;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/members/new")
    public MemberForm createForm(){
        MemberForm form =new MemberForm();
        return form;
    }

    @PostMapping("/members/new")
    public ResponseEntity<String> create(@RequestBody MemberForm form){
        Member member =new Member();
        member.setEmail(form.getEmail());
        member.setPw(form.getPw());
        member.setName(form.getName());
        member.setGender(form.getGender());

        memberService.join(member);
        return new ResponseEntity<String>("회원가입에 성공", HttpStatus.OK);
    }
}
