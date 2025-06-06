package angrymiaucino.locationservice.config.cache;

import java.time.Duration;

public record CacheProperties(String cacheName, Duration expiryPolicy) {}
