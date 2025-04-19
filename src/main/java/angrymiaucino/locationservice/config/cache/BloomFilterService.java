package angrymiaucino.locationservice.config.cache;

import angrymiaucino.locationservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class BloomFilterService {
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final RedisBloomFilterConfig redisBloomFilterConfig;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(BloomFilterService.class);

    public BloomFilterService(ReactiveRedisTemplate<String, String> redisTemplate, RedisBloomFilterConfig redisBloomFilterConfig, UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.redisBloomFilterConfig = redisBloomFilterConfig;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        initBloomFilter(BloomFilterName.USER_NAME)
                .then(preloadFromDbPaginated(0, 1000))
                .subscribe();
    }

    public Mono<Void> preloadFromDbPaginated(int page, int size) {
        return userRepository.findUsernamesBy(page * size, size)
                .collectList()
                .flatMap(batch -> handle(batch, page, size));
    }

    private Mono<Void> handle(List<String> batch, int page, int size) {
        if (batch.isEmpty()) {
            return Mono.empty();
        } else {
            return addBatchToBloomFilter(batch)
                    .then(preloadFromDbPaginated(page + 1, size));
        }
    }

    public Mono<Void> addBatchToBloomFilter(List<String> batch) {
        var script = RedisScript.of(
                "for i=1,#ARGV do redis.call('BF.ADD', KEYS[1], ARGV[i]) end; return 1",
                Long.class
        );
        return redisTemplate.execute(script, List.of(redisBloomFilterConfig.getBloomProperties().get(BloomFilterName.USER_NAME).name()), batch)
                .then();
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

        List<String> keys = List.of(config.name());
        List<String> args = List.of(String.valueOf(config.falsePositivesRate()), String.valueOf(config.expectedItems()));

        return redisTemplate.execute(redisScript, keys, args)
                .next()
                .map(result -> result == 1L)
                .doOnTerminate(() -> logger.info("Bloom Filter initialized successfully: {}", config.name()))
                .onErrorResume(Mono::error);
    }

    public Mono<Boolean> add(String data, BloomFilterName filterKey) {
        var script = RedisScript.of("return redis.call('BF.ADD', KEYS[1], ARGV[1])", Long.class);
        return redisTemplate.execute(script, List.of(redisBloomFilterConfig.getBloomProperties().get(filterKey).name()), List.of(data))
                .next()
                .map(result -> result == 1L); // true if added successfully
    }

    public Mono<Boolean> check(String data, BloomFilterName filterKey) {
        var script = RedisScript.of("return redis.call('BF.EXISTS', KEYS[1], ARGV[1])", Long.class);
        return redisTemplate.execute(script, List.of(redisBloomFilterConfig.getBloomProperties().get(filterKey).name()), List.of(data))
                .next()
                .map(result -> result == 1L);
    }
}
