package angrymiaucino.locationservice.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "infinispan")
public class InfinispanConfig {

    private boolean enabled;
    private Map<CacheName, CacheConfig> cache;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<CacheName, CacheConfig> getCache() {
        return cache;
    }

    public void setCache(Map<CacheName, CacheConfig> cache) {
        this.cache = cache;
    }

    public static class CacheConfig {

        private CacheMode mode;
        private long lifespan;
        private int size;

        public CacheMode getMode() {
            return mode;
        }

        public void setMode(CacheMode mode) {
            this.mode = mode;
        }

        public long getLifespan() {
            return lifespan;
        }

        public void setLifespan(long lifespan) {
            this.lifespan = lifespan;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}

