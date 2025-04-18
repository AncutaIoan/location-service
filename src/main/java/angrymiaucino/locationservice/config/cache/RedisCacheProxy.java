package angrymiaucino.locationservice.config.cache;

import org.redisson.api.RMapCacheReactive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Collection;

public class RedisCacheProxy<K, V> {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheProxy.class);

    private final RMapCacheReactive<K, V> cache;
    private final CacheProperties props;
    private final Duration defaultTtl = Duration.ofMinutes(10);

    public RedisCacheProxy(RMapCacheReactive<K, V> cache, CacheProperties props) {
        this.cache = cache;
        this.props = props;
    }

    public Mono<Map<K, V>> getAll(Set<K> keys) {
        return cache.getAll(keys);
    }

    public Mono<V> get(K key) {
        return cache.get(key);
    }

    public Mono<V> set(K key, V value) {
        long ttl = props != null && props.expiryPolicy() != null
                ? props.expiryPolicy().toMillis()
                : defaultTtl.toMillis();

        return cache.fastPut(key, value, ttl, TimeUnit.MILLISECONDS)
                .thenReturn(value)
                .doOnSuccess(v -> {
                    log.debug("[{}]: Caching for key=<{}>", name(), key);
                    log.trace("[{}]: Caching value=<{}> for key=<{}>", name(), value, key);
                });
    }

    public Mono<Void> putAll(Map<K, V> values) {
        long ttl = props != null && props.expiryPolicy() != null
                ? props.expiryPolicy().toMillis()
                : defaultTtl.toMillis();

        return cache.putAll(values, ttl, TimeUnit.MILLISECONDS)
                .doOnSuccess(v -> {
                    log.debug("[{}]: Caching for keys=<{}>", name(), values.keySet());
                    log.trace("[{}]: Caching values=<{}>", name(), values);
                });
    }

    public Mono<Boolean> containsKey(K key) {
        return cache.containsKey(key)
                .onErrorResume(e -> {
                    log.warn("[{}]: Potentially uninitialized", name());
                    return Mono.just(false);
                });
    }

    public String name() {
        return cache.getName();
    }

    public Mono<Integer> size() {
        return cache.size()
                .onErrorResume(e -> {
                    log.warn("[{}]: Potentially uninitialized", name());
                    return Mono.just(0);
                });
    }

    public Mono<Collection<V>> getAll() {
        return cache.readAllValues();
    }

    public Mono<Set<Map.Entry<K, V>>> entries() {
        return cache.readAllEntrySet();
    }

    public Mono<Long> delete(K key) {
        return cache.fastRemove(key);
    }

    public Mono<V> getAndDelete(K key) {
        return cache.remove(key);
    }

    public Mono<Boolean> clear() {
        return cache.delete()
                .onErrorResume(e -> {
                    log.warn("[{}]: Potentially uninitialized", name());
                    return Mono.just(false);
                });
    }

    public Mono<V> cached(K key, java.util.function.Supplier<Mono<V>> supplier) {
        return get(key)
                .flatMap(cachedValue -> {
                    log.debug("[{}]: Using cached value for key=<{}>", name(), key);
                    return Mono.just(cachedValue);
                })
                .switchIfEmpty(supplier.get()
                        .flatMap(value -> {
                            log.debug("[{}]: No cache value found for key=<{}>, fetching via provided supplier", name(), key);
                            return set(key, value);
                        }));
    }
}
