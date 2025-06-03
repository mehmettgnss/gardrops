package com.gardrops.imageupload.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class SessionService {

    private final RedisTemplate<String, String> redisTemplate;
    private final int sessionTtlHours;
    private final int maxImagesPerSession;

    public SessionService(
            RedisTemplate<String, String> redisTemplate,
            @Value("${app.session.ttl-hours}") int sessionTtlHours,
            @Value("${app.session.max-images}") int maxImagesPerSession) {
        this.redisTemplate = redisTemplate;
        this.sessionTtlHours = sessionTtlHours;
        this.maxImagesPerSession = maxImagesPerSession;
    }

    public UUID createSession() {
        UUID sessionId = UUID.randomUUID();
        String key = "session:" + sessionId;
        redisTemplate.opsForValue().set(key, "active", Duration.ofHours(sessionTtlHours));
        return sessionId;
    }

    public boolean isSessionValid(UUID sessionId) {
        String key = "session:" + sessionId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean canAddImage(UUID sessionId) {
        String key = "session:" + sessionId + ":images";
        Long size = redisTemplate.opsForSet().size(key);
        return size != null && size < maxImagesPerSession;
    }

    public void addImage(UUID sessionId, UUID imageId) {
        String key = "session:" + sessionId + ":images";
        redisTemplate.opsForSet().add(key, imageId.toString());
        redisTemplate.expire(key, Duration.ofHours(sessionTtlHours));
    }

    public void removeImage(UUID sessionId, UUID imageId) {
        String key = "session:" + sessionId + ":images";
        redisTemplate.opsForSet().remove(key, imageId.toString());
    }

    public Set<UUID> getSessionImages(UUID sessionId) {
        String key = "session:" + sessionId + ":images";
        Set<String> imageIds = redisTemplate.opsForSet().members(key);
        Set<UUID> result = new HashSet<>();
        if (imageIds != null) {
            for (String id : imageIds) {
                result.add(UUID.fromString(id));
            }
        }
        return result;
    }
} 