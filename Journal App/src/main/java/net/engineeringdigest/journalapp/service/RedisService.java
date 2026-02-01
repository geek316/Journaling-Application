package net.engineeringdigest.journalapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(obj.toString(), entityClass);
        } catch(Exception e) {
            log.error("Exception ", e.getMessage());
            return null;
        }
    }

    public void set(String key, Object obj, Long ttl) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(obj);
            redisTemplate.opsForValue().set(key, json, ttl, TimeUnit.SECONDS);
        } catch(Exception e) {
            log.error("Exception ", e.getMessage());
        }
    }
}
