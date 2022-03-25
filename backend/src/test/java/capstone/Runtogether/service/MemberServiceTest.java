package capstone.Runtogether.service;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.dto.MemberDto;
import capstone.Runtogether.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
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
        MemberDto memberDTO = MemberDto.builder()
                .name("연잔")
                .email("abcd@naver.com")
                .pwd("1234")
                .gender('F')
                .build();

        //when
        String saveId = memberService.join(memberDTO);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(memberDTO.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_닉네임_예외(){
        //given
        MemberDto memberDto1 = MemberDto.builder()
                .name("spring")
                .email("a@naver.com")
                .pwd(passwordEncoder.encode("1234"))
                .gender('F')
                .build();

        MemberDto memberDto2 = MemberDto.builder()
                .name("spring")
                .email("abc@naver.com")
                .pwd(passwordEncoder.encode("1234"))
                .gender('F')
                .build();
        //when
        memberService.join(memberDto1);

        //then
        Assertions.assertThat(memberService.join(memberDto1)).isEqualTo("existName");
    }
    @Test
    public void 중복_회원_예외(){
        //given
        MemberDto memberDto1 = MemberDto.builder()
                .name("spring")
                .email("a@naver.com")
                .pwd(passwordEncoder.encode("1234"))
                .gender('F')
                .build();

        MemberDto memberDto2 = MemberDto.builder()
                .name("boot")
                .email("a@naver.com")
                .pwd(passwordEncoder.encode("1234"))
                .gender('F')
                .build();

        //when
        memberService.join(memberDto1);

        //then
        Assertions.assertThat(memberService.join(memberDto1)).isEqualTo("existEmail");

    }
}