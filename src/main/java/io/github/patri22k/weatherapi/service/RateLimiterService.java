package io.github.patri22k.weatherapi.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimiterService {

    private final Map<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 5;

    public boolean isAllowed(String ipAddress) {
        requestCountsPerIpAddress.putIfAbsent(ipAddress, new AtomicInteger(0));
        int count = requestCountsPerIpAddress.get(ipAddress).incrementAndGet();
        return count <= MAX_REQUESTS_PER_MINUTE;
    }

    // Reset every 60 seconds
    @Scheduled(fixedRate = 60_000)
    public void resetRequestCounts() {
        requestCountsPerIpAddress.clear();
    }

}
