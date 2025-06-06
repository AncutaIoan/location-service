package angrymiaucino.locationservice.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisCacheProperties {
    private Map<RedisCacheName, CacheProperties> cacheProperties = new HashMap<>();

    public Map<RedisCacheName, CacheProperties> getCacheProperties() {
        return cacheProperties;
    }

    public void setCacheProperties(Map<RedisCacheName, CacheProperties> cacheProperties) {
        this.cacheProperties = cacheProperties;
    }
}


