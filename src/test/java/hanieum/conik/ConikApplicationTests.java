package hanieum.conik;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@SpringBootTest
class ConikApplicationTests {

    // 1. RedisConfig에서 @Qualifier("chatPubSub")로 등록된 공장 대체
    @MockBean(name = "chatPubSubFactory")
    private RedisConnectionFactory chatPubSubFactory;

    // 2. RedisConfig에서 등록된 기본 공장 대체
    @MockBean(name = "redisConnectionFactory")
    private RedisConnectionFactory redisConnectionFactory;

    // 3. [추가!] ChatModifyService가 주입받으려는 Template 자체를 가짜로 대체
    // 이 부분이 없으면 스프링이 진짜 빈을 만들려다 Qualifier 에러를 냅니다.
    @MockBean(name = "stringRedisTemplate")
    @Qualifier("chatPubSub")
    private StringRedisTemplate stringRedisTemplate;

    // 4. 리스너 컨테이너 대체
    @MockBean(name = "redisMessageListenerContainer")
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Test
    void contextLoads() {
    }
}
