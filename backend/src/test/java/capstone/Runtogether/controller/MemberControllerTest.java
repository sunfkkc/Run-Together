package capstone.Runtogether.controller;

import capstone.Runtogether.config.SecurityConfig;
import capstone.Runtogether.config.SpringConfig;
import capstone.Runtogether.dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@ContextConfiguration(classes = {SpringConfig.class, SecurityConfig.class})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 회원가입_POST() throws Exception {
        //given
        MemberDto memberDto = MemberDto.builder()
                .email("abcd@naver.com")
                .name("연잔")
                .pwd("1234")
                .gender('F')
                .build();

        String content = objectMapper.writeValueAsString(memberDto);

        //when
        mockMvc.perform(
                        post("/api/join")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        //then


    }

    @Test
    void login() {
    }
}