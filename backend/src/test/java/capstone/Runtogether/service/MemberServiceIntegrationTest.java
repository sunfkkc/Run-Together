package capstone.Runtogether.service;

import capstone.Runtogether.entity.Member;
import capstone.Runtogether.dto.MemberDto;
import capstone.Runtogether.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        MemberDto memberDto = MemberDto.builder()
                .email("abcd@naver.com")
                .name("연잔")
                .pwd("1234")
                .gender('F')
                .build();

        //when
        String saveMemberEmail = memberService.join(memberDto);

        //then
        Optional<Member> findMember = memberService.findByEmail(saveMemberEmail);
        assertThat(findMember.get().getName()).isEqualTo(memberDto.getName());
    }

    @Test
    void 중복_닉네임_예외(){
        //given
        MemberDto memberDto1 = MemberDto.builder()
                .email("a@naver.com")
                .name("spring")
                .pwd("1234")
                .gender('F')
                .build();

        MemberDto memberDto2 = MemberDto.builder()
                .email("b@naver.com")
                .name("spring")
                .pwd("5678")
                .gender('M')
                .build();
        //when
        memberService.join(memberDto1);

        //then
        assertThat(memberService.join(memberDto2)).isEqualTo("existName");
    }
    @Test
    void 중복_계정_예외(){
        //given
        MemberDto memberDto1 = MemberDto.builder()
                .email("a@naver.com")
                .name("spring")
                .pwd("1234")
                .gender('F')
                .build();

        MemberDto memberDto2 = MemberDto.builder()
                .email("a@naver.com")
                .name("summer")
                .pwd("5678")
                .gender('M')
                .build();

        //when
        memberService.join(memberDto1);

        //then
        assertThat(memberService.join(memberDto2)).isEqualTo("existEmail");

    }
    @Test
    void 로그인(){

    }

}