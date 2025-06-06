package angrymiaucino.locationservice.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.data.redis.bloom-filters")
public class RedisBloomFilterConfig {
    private Map<BloomFilterName, BloomFilterProperties> bloomProperties = new HashMap<>();

    public Map<BloomFilterName, BloomFilterProperties> getBloomProperties() {
        return bloomProperties;
    }

    public void setBloomProperties(Map<BloomFilterName, BloomFilterProperties> bloomProperties) {
        this.bloomProperties = bloomProperties;
    }
}
