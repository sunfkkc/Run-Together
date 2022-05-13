package capstone.Runtogether.repository;

import capstone.Runtogether.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisBlackListRepository {

    @Autowired
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
