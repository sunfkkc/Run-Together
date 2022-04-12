package capstone.Runtogether.repository;

import capstone.Runtogether.util.JwtTokenProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisBlackListRepository {

    private final RedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public RedisBlackListRepository(RedisTemplate redisTemplate, JwtTokenProvider jwtTokenProvider) {
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void set(String token){
        redisTemplate.opsForValue()
                .set(token,"access-token",
                        jwtTokenProvider.getAccessTokenExpirationTime(), TimeUnit.MILLISECONDS);

    }
}
