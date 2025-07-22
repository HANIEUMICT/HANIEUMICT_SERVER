package hanieum.conik.global.adapter.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "refreshToken:";

    // 1) 저장
    public void saveRefreshToken(Long memberId, String refreshToken, long expirationMinutes) {
        String key = KEY_PREFIX + memberId;
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();

        hashOps.put(key, "token", refreshToken);
        hashOps.put(key, "memberId", memberId.toString());

        redisTemplate.expire(key, Duration.ofMinutes(expirationMinutes)); // 만료시간 설정
    }

    // 2) 조회
    public String getRefreshToken(Long memberId) {
        String key = KEY_PREFIX + memberId;
        return (String) redisTemplate.opsForHash().get(key, "token");
    }

    // 3) 삭제
    public void deleteRefreshToken(Long memberId) {
        String key = KEY_PREFIX + memberId;
        redisTemplate.delete(key);
    }

    // 4) 검증
    public boolean isRefreshTokenValid(Long memberId, String refreshToken) {
        String storedToken = getRefreshToken(memberId);
        return storedToken != null && storedToken.equals(refreshToken);
    }

}
