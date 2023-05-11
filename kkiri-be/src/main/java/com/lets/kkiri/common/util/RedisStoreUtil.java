package com.lets.kkiri.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisStoreUtil {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * redis에 데이터를 저장한다.
     * @param key
     * @param data
     * @param <T>
     * @return
     */
    public <T> boolean saveData(String key, T data) {
        try {
            String value = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * redis에 저장된 데이터를 가져온다.
     * @param key
     * @param classType
     * @param <T>
     * @return
     */
    public <T> Optional<T> getData(String key, Class<T> classType) {
        String value = redisTemplate.opsForValue().get(key);

        if(value == null){
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(value, classType));
        } catch(Exception e){
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    public <T> boolean updateData(String key, T data) {
        try {
            String value = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().append(key, value);
            return true;
        } catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}
