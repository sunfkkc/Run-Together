package capstone.Runtogether.controller;

import capstone.Runtogether.entity.Member;
import capstone.Runtogether.dto.LoginFormDto;
import capstone.Runtogether.dto.MemberDto;
import capstone.Runtogether.model.Response;
import capstone.Runtogether.model.StatusCode;
import capstone.Runtogether.service.MemberService;
import capstone.Runtogether.service.UserDetailServiceImpl;
import capstone.Runtogether.util.CookieUtil;
import capstone.Runtogether.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController extends ApiBaseController {

    private final UserDetailServiceImpl userDetailService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final CookieUtil cookieUtil;
    private Object status;


    @PostMapping("join")
    public ResponseEntity<String> join(@RequestBody MemberDto memberDto) {
        String result = memberService.join(memberDto);
        if (result.equals("existEmail")) {
            return new ResponseEntity<>("이미 존재하는 계정입니다.", HttpStatus.OK);
        } else if (result.equals("existName")) {
            return new ResponseEntity<>("이미 존재하는 이름입니다.", HttpStatus.OK);
        }
        return new ResponseEntity<>("회원가입에 성공", HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginFormDto form, HttpServletResponse response) {
        Member loginMember = userDetailService.loadUserByUsername(form.getEmail());

        if (!passwordEncoder.matches(form.getPwd(), loginMember.getPassword())) {
            return new ResponseEntity<>("비밀번호가 올바르지 않습니다", HttpStatus.FORBIDDEN);
        }
        //토큰 발급
        String accessToken = jwtTokenProvider.generateAccessToken(loginMember);
        //쿠키에 토큰 넣기
        Cookie tokenCookie = cookieUtil.createCookie("auth",accessToken);

        response.addCookie(tokenCookie);

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }

    @GetMapping("checkEmail")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email) {
        return memberService.checkEmail(email);
    }

    @GetMapping("checkName")
    public ResponseEntity<?> checkName(@RequestParam("name") String name) {
        return memberService.checkName(name);
    }

    @GetMapping("")
    public ResponseEntity<?> afterLogin(HttpServletRequest request) {
        Cookie auth = cookieUtil.getCookie(request, "auth");
        String accessToken = auth.getValue();
        String email = jwtTokenProvider.getEmailFromJwt(accessToken);
        return new ResponseEntity<>(email, HttpStatus.OK);

    }

    @PostMapping("logout")
    public ResponseEntity<Response<Object>> logout(HttpServletRequest request) {
        Cookie logoutToken = cookieUtil.getCookie(request,"auth");
        redisTemplate.opsForValue()
                .set(logoutToken.getValue(), "access-token",
                        60*60, TimeUnit.SECONDS);

        return new ResponseEntity<>(new Response<>(StatusCode.OK,"로그아웃 완료"),HttpStatus.OK);
    }

       /* @PostMapping("reissue")
    public ResponseEntity<?> reissue(@RequestBody ReissueDto reissue, HttpServletResponse response){
        if(!jwtTokenProvider.validateToken(reissue.getRefreshToken())){
            return new ResponseEntity<String>("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.OK);
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        String refreshToken = (String) redisTemplate.opsForValue().get("RT:"+authentication.getName());
        if(!refreshToken.equals(reissue.getRefreshToken())){
            return new ResponseEntity<String>("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.OK);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken((Member) authentication);

        return new ResponseEntity<String>(newAccessToken, HttpStatus.OK);
    }*/
}
