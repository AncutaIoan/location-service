package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.config.cache.CacheProperties;
import angrymiaucino.locationservice.config.cache.RedisCacheProxy;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RedisCacheProxyTest {

    private final RMapCacheReactive<String, String> cache = mock();
    private final CacheProperties props = mock();
    private final RedisCacheProxy<String, String> proxy = new RedisCacheProxy<>(cache, props);

    @Test
    void get_withExistingKey_returnsCachedValue() {
        String key = "testKey";
        String value = "cachedValue";
        when(cache.get(key)).thenReturn(Mono.just(value));

        String result = proxy.get(key).block();

        assertThat(result).isEqualTo(value);
    }

    @Test
    void getAll_withExistingKeys_returnsCachedValues() {
        Set<String> keys = Set.of("k1", "k2");
        Map<String, String> values = Map.of("k1", "v1", "k2", "v2");
        when(cache.getAll(keys)).thenReturn(Mono.just(values));

        Map<String, String> result = proxy.getAll(keys).block();

        assertThat(result).isEqualTo(values);
    }

    @Test
    void getAll_withMissingKeys_returnsNull() {
        Set<String> keys = Set.of("k1", "k2");
        when(cache.getAll(keys)).thenReturn(Mono.empty());

        Map<String, String> result = proxy.getAll(keys).block();

        assertThat(result).isNull();
    }

    @Test
    void set_withValidKeyAndValue_storesAndReturnsValue() {
        String key = "testKey";
        String value = "newValue";
        when(cache.fastPut(eq(key), eq(value), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(Mono.just(true));
        when(cache.getName()).thenReturn("testCache");

        String result = proxy.set(key, value).block();

        assertThat(result).isEqualTo(value);
    }

    @Test
    void putAll_withValidMap_storesAllValues() {
        Map<String, String> values = Map.of("a", "1", "b", "2");
        when(cache.putAll(eq(values), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(Mono.empty());
        when(cache.getName()).thenReturn("testCache");

        proxy.putAll(values).block();

        verify(cache).putAll(eq(values), anyLong(), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    void containsKey_withExistingKey_returnsTrue() {
        when(cache.containsKey("k")).thenReturn(Mono.just(true));

        Boolean result = proxy.containsKey("k").block();

        assertThat(result).isTrue();
    }

    @Test
    void containsKey_withError_returnsFalse() {
        when(cache.getName()).thenReturn("testCache");
        when(cache.containsKey("k")).thenReturn(Mono.error(new RuntimeException("fail")));

        Boolean result = proxy.containsKey("k").block();

        assertThat(result).isFalse();
    }

    @Test
    void size_returnsCorrectSize() {
        when(cache.size()).thenReturn(Mono.just(42));

        Integer result = proxy.size().block();

        assertThat(result).isEqualTo(42);
    }

    @Test
    void size_withError_returnsZero() {
        when(cache.getName()).thenReturn("testCache");
        when(cache.size()).thenReturn(Mono.error(new RuntimeException("fail")));

        Integer result = proxy.size().block();

        assertThat(result).isEqualTo(0);
    }

    @Test
    void getAll_returnsAllValues() {
        List<String> values = List.of("a", "b");
        when(cache.readAllValues()).thenReturn(Mono.just(values));

        Collection<String> result = proxy.getAll().block();

        assertThat(result).isEqualTo(values);
    }

    @Test
    void getAndDelete_removesAndReturnsValue() {
        String key = "k";
        String value = "v";
        when(cache.remove(key)).thenReturn(Mono.just(value));

        String result = proxy.getAndDelete(key).block();

        assertThat(result).isEqualTo(value);
    }

    @Test
    void cached_withExistingKey_returnsCachedValue() {
        String key = "k";
        String value = "v";
        when(cache.get(key)).thenReturn(Mono.just(value));
        when(cache.getName()).thenReturn("testCache");

        String result = proxy.cached(key, () -> Mono.just("newValue")).block();

        assertThat(result).isEqualTo(value);
    }

    @Test
    void cached_withMissingKey_fetchesAndStoresValue() {
        String key = "k";
        String value = "newVal";
        when(cache.get(key)).thenReturn(Mono.empty());
        when(cache.getName()).thenReturn("testCache");
        when(cache.fastPut(eq(key), eq(value), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(Mono.just(true));

        String result = proxy.cached(key, () -> Mono.just(value)).block();

        assertThat(result).isEqualTo(value);
    }
}
