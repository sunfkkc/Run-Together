package capstone.Runtogether.util;

import capstone.Runtogether.domain.Member;
import capstone.Runtogether.service.MemberService;
import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {

    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
    //String secretString = Encoders.BASE64.encode(key.getEncoded());

    private final MemberService memberService;

    // 토큰 유효시간
    private final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30; // 30분간 토큰 유효
    private final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 14; //2주간 토큰 유효

    @Autowired
    public JwtTokenProvider(MemberService memberService) {
        this.memberService = memberService;
    }

    //Member 통해 RefreshToken 생성 및 반환
    public String generateRefreshToken(Member member){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "REFRESH_TOKEN")
                .setHeaderParam("alg", "HS256")
                .setSubject(member.getMemberId().toString())
                .setIssuedAt(now) //토큰 발행 시간
                .setExpiration(new Date(now.getTime()+REFRESH_TOKEN_EXPIRE_TIME)) //토큰 유호 시간
                .claim("role",member.getRole().toString())
                .signWith(key) // 승인 암호화 알고리즘
                .compact();
    }

    //Member 통해 AccessToken 생성 및 반환
    public String generateAccessToken(Member member){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "ACCESS_TOKEN")
                .setHeaderParam("alg", "HS256")
                .setSubject(member.getMemberId().toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .claim("role", member.getRole().toString())
                .signWith(key)
                .compact();
    }

    // Jwt 토큰에서 memberId 추출
    public Long getMemberIdFromJwt(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long memberId = Long.valueOf(claims.getSubject());

        return memberId;

    }

    //Jwt Token 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        } catch (NullPointerException ex){
            log.error("JWT RefreshToken is empty");
        }
        return false;
    }



    // Jwt Token에 담긴 유저 정보를 DB에 검색
    // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 반환
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = memberService.loadUserByUsername(String.valueOf(getMemberIdFromJwt(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }


/*
    // Request의 Header에서 token 값 추출
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("AUTH");
    }

*/





}
