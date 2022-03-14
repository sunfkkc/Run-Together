package capstone.Runtogether.controller;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form){
        Member member =new Member();
        member.setEmail(form.getEmail());
        member.setEmail(form.getEmail());
        member.setName(form.getName());
        member.setPw(form.getPw());
        member.setGender(form.getGender());

        memberService.join(member);

        return "redirect:/";
    }
}
