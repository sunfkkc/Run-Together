package capstone.Runtogether.util;

import capstone.Runtogether.entity.Member;
import capstone.Runtogether.service.UserDetailServiceImpl;
import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
    //String secretString = Encoders.BASE64.encode(key.getEncoded());

    private final UserDetailServiceImpl userDetailService;
    private final RedisTemplate redisTemplate;

    // 토큰 유효시간
    //private final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 30 ; // 30초간 토큰 유효
    public final static Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60; // 60분간 토큰 유효
    public final static Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 14; //2주간 토큰 유효

    @Autowired
    public JwtTokenProvider(RedisTemplate<String, String> redisTemplate, UserDetailServiceImpl userDetailService, RedisTemplate redisTemplate1) {
        this.userDetailService = userDetailService;
        this.redisTemplate = redisTemplate1;
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
                .setSubject(member.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .claim("role", member.getRole().toString())
                .signWith(key)
                .compact();
    }

    // Jwt 토큰에서 email 추출
    public String getEmailFromJwt(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email = String.valueOf(claims.getSubject());

        return email;

    }

    //Jwt Token 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            if(redisTemplate.hasKey(token)){
                return false;
            }
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
        Member userDetails = userDetailService.loadUserByUsername(String.valueOf(getEmailFromJwt(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    public Long getAccessTokenExpirationTime(){
        return ACCESS_TOKEN_EXPIRE_TIME;
    }


}
