package capstone.Runtogether.service;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.dto.MemberDTO;
import capstone.Runtogether.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    void 회원가입() {
        //given
        MemberDTO memberDTO =MemberDTO.builder()
                .name("연잔")
                .email("abcd@naver.com")
                .pw("1234")
                .gender('F')
                .build();

        //when
        Long saveId = memberService.join(memberDTO).getId();

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(memberDTO.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_닉네임_예외(){
        //given
        MemberDTO memberDTO1 = MemberDTO.builder()
                .name("spring")
                .email("a@naver.com")
                .pw(passwordEncoder.encode("1234"))
                .gender('F')
                .build();

        MemberDTO memberDTO2 = MemberDTO.builder()
                .name("spring")
                .email("abc@naver.com")
                .pw(passwordEncoder.encode("1234"))
                .gender('F')
                .build();
        //when
        memberService.join(memberDTO1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(memberDTO2));

        //then
    }
    @Test
    public void 중복_회원_예외(){
        //given
        MemberDTO memberDTO1 = MemberDTO.builder()
                .name("spring")
                .email("a@naver.com")
                .pw(passwordEncoder.encode("1234"))
                .gender('F')
                .build();

        MemberDTO memberDTO2 = MemberDTO.builder()
                .name("boot")
                .email("a@naver.com")
                .pw(passwordEncoder.encode("1234"))
                .gender('F')
                .build();

        //when
        memberService.join(memberDTO1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(memberDTO2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }
}