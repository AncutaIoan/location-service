package angrymiaucino.locationservice.config.cache;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class BloomFilterStarter {
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final RedisBloomFilterConfig  redisBloomFilterConfig;
    private final Logger logger = LoggerFactory.getLogger(BloomFilterStarter.class);

    public BloomFilterStarter(ReactiveRedisTemplate<String, String> redisTemplate, RedisBloomFilterConfig redisBloomFilterConfig) {
        this.redisTemplate = redisTemplate;
        this.redisBloomFilterConfig = redisBloomFilterConfig;
    }

    @PostConstruct
    public void init() {
        initBloomFilter(BloomFilterName.USER_NAME)  // Use "USER_NAME" or any other Bloom Filter key defined in the config
                .subscribe();
    }

    public Mono<Boolean> initBloomFilter(BloomFilterName filterKey) {
        BloomFilterProperties config = redisBloomFilterConfig.getBloomProperties().get(filterKey);

        if (config == null) {
            return Mono.error(new IllegalArgumentException("Bloom Filter configuration not found for key: "+ filterKey));
        }

        String script = """
                if redis.call('EXISTS', KEYS[1]) == 0 then
                    redis.call('BF.RESERVE', KEYS[1], ARGV[1], ARGV[2])
                    return 1
                else
                    return 0
                end
            """;

        RedisScript<Long> redisScript = RedisScript.of(script, Long.class);

        List<String> keys = List.of(config.name()); // Using name from the config
        List<String> args = List.of(String.valueOf(config.falsePositivesRate()), String.valueOf(config.expectedItems()));

        return redisTemplate.execute(redisScript, keys, args)
                .next()
                .map(result -> result == 1L)
                .doOnTerminate(() -> logger.info("Bloom Filter initialized successfully: {}", config.name()))
                .onErrorResume(Mono::error);
    }
}
