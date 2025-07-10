package hanieum.conik.global.adapter.redis;

import hanieum.conik.global.apiPayload.exception.GlobalErrorType;
import hanieum.conik.global.apiPayload.exception.GlobalException;
import hanieum.conik.global.application.required.MemoryMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisMemoryMap implements MemoryMap {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setValue(String key, String value, Long timeout) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            values.set(key, value, Duration.ofMillis(timeout));
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorType.REDIS_SET_ERROR);
        }
    }

    @Override
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

    @Override
    public void deleteValue(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorType.REDIS_DELETE_ERROR);
        }
    }

    @Override
    public boolean checkExistsValue(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorType.REDIS_GET_ERROR);
        }
    }
}
