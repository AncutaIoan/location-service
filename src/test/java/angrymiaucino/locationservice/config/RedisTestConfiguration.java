package angrymiaucino.locationservice.config;

import angrymiaucino.locationservice.common.dto.UserDTO;
import angrymiaucino.locationservice.config.cache.RedisCacheName;
import angrymiaucino.locationservice.config.cache.RedisCacheProxy;
import angrymiaucino.locationservice.repository.entity.Place;
import org.mockito.Mockito;
import org.redisson.api.RMapCacheReactive;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("test")
public class RedisTestConfiguration {

    @Bean("userDTOCache")
    @Primary
    public RedisCacheProxy<Long, UserDTO> userDTOCache() {
        return mockRedisCacheProxy();
    }

    @Bean("placeCache")
    @Primary
    public RedisCacheProxy<Long, Place> placeCache() {
        return mockRedisCacheProxy();
    }

    @Bean
    @Primary
    public Map<RedisCacheName, RedisCacheProxy<?, ?>> redisCaches() {
        return Map.of(
                RedisCacheName.USERS_BY_ID, mockRedisCacheProxy(),
                RedisCacheName.PLACES_BY_ID, mockRedisCacheProxy()
        );
    }

    public static <K, V> RedisCacheProxy<K, V> mockRedisCacheProxy() {
        RMapCacheReactive<K, V> cacheReactive = mock();
        RedisCacheProxy<K, V> mockProxy = Mockito.spy(new RedisCacheProxy<>(cacheReactive, mock()));

        Mockito.when(cacheReactive.fastPut(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Mono.empty());
        Mockito.when(cacheReactive.putAll(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Mono.empty());
        Mockito.when(cacheReactive.remove(Mockito.any()))
                .thenReturn(Mono.empty());
        Mockito.when(cacheReactive.get(Mockito.any()))
                .thenReturn(Mono.empty());
        Mockito.when(cacheReactive.getAll(Mockito.any()))
                .thenReturn(Mono.empty());
        Mockito.when(cacheReactive.fastRemove(Mockito.any()))
                .thenReturn(Mono.just(1L));
        Mockito.when(cacheReactive.getName())
                .thenReturn("test-cache");
        Mockito.when(cacheReactive.containsKey(Mockito.any()))
                .thenReturn(Mono.just(false));
        Mockito.when(cacheReactive.readAllValues())
                .thenReturn(Mono.just(java.util.Collections.emptyList()));
        Mockito.when(cacheReactive.readAllEntrySet())
                .thenReturn(Mono.just(new java.util.HashSet<>()));
        Mockito.when(cacheReactive.delete())
                .thenReturn(Mono.just(true));

        return mockProxy;
    }
}

