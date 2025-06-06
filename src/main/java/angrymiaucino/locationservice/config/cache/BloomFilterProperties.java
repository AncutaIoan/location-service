package angrymiaucino.locationservice.config.cache;

public record BloomFilterProperties(String name, int expectedItems, double falsePositivesRate) {}

