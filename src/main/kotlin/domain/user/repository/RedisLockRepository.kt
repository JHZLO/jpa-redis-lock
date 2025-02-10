package org.example.domain.user.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisLockRepository(private val redisTemplate: RedisTemplate<String, String>) {

    fun lock(key: Long): Boolean {
        return redisTemplate
            .opsForValue()
            .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3000)) == true
    }

    fun unlock(key: Long): Boolean {
        return redisTemplate.delete(generateKey(key)) ?: false
    }

    private fun generateKey(key: Long): String {
        return key.toString()
    }
}
