package angrymiaucino.locationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "infinispan.embedded.cache")
public class CacheConfigProperties {
    private Map<String, CacheProperties> caches;

    public Map<String, CacheProperties> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, CacheProperties> caches) {
        this.caches = caches;
    }

    public static class CacheProperties {
        private String mode;
        private Expiration expiration;
        private Memory memory;

        // Getters and setters

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Expiration getExpiration() {
            return expiration;
        }

        public void setExpiration(Expiration expiration) {
            this.expiration = expiration;
        }

        public Memory getMemory() {
            return memory;
        }

        public void setMemory(Memory memory) {
            this.memory = memory;
        }

        public static class Expiration {
            private long lifespan;

            // Getter and setter
            public long getLifespan() {
                return lifespan;
            }

            public void setLifespan(long lifespan) {
                this.lifespan = lifespan;
            }
        }

        public static class Memory {
            private int size;

            // Getter and setter
            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }
        }
    }
}
