package hanieum.conik.global.clients.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import hanieum.conik.global.apiPayload.exception.GlobalException;
import hanieum.conik.global.apiPayload.exception.GlobalErrorType;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisClient {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, String value, Long timeout) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            values.set(key, value, Duration.ofMillis(timeout));
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorType.REDIS_SET_ERROR);
        }
    }

    public String getValue(String key) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            if (values.get(key) == null) {
                return "";
            }
            return values.get(key).toString();
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorType.REDIS_GET_ERROR);
        }
    }

    public void deleteValue(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorType.REDIS_DELETE_ERROR);
        }
    }

    public boolean checkExistsValue(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorType.REDIS_GET_ERROR);
        }
    }
}