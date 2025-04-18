package angrymiaucino.locationservice.config.cache;

import angrymiaucino.locationservice.common.dto.UserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static angrymiaucino.locationservice.config.cache.RedisCacheName.USERS_BY_ID;

@Configuration
@Profile("!test")
@EnableConfigurationProperties(RedisCacheProperties.class)
public class RedisConfig {

    private final ObjectMapper objectMapper;

    public RedisConfig() {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public RedissonReactiveClient redissonReactiveClient(RedisProperties props) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + props.getHost() + ":" + props.getPort())
                .setUsername(props.getUsername())
                .setPassword(props.getPassword());
        return Redisson.create(config).reactive();
    }

    @Bean(name = "userDTOCache")
    public RedisCacheProxy<Long, UserDTO> userDTOCache(RedissonReactiveClient client, RedisCacheProperties props) {
        return createCache(USERS_BY_ID, client, props, new TypeReference<Long>() {}, new TypeReference<UserDTO>() {});
    }

    public <K, V> RedisCacheProxy<K, V> createCache(
            RedisCacheName name,
            RedissonReactiveClient client,
            RedisCacheProperties props,
            TypeReference<K> keyType,
            TypeReference<V> valueType
    ) {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(keyType, valueType, objectMapper);
        var cacheProps = props.getCacheProperties().get(name);
        return new RedisCacheProxy<>(client.getMapCache(cacheProps.cacheName(), codec), cacheProps);
    }
}
