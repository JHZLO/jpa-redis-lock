package org.example.domain.user.repository

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit


@Repository
class RedisLockRepository(
    private val redissonClient: RedissonClient
) {
    fun lock(key: Long): Boolean {
        val lock = redissonClient.getLock("lock_key_$key")
        return lock.tryLock(10, 1, TimeUnit.SECONDS)  // 락을 10초 동안 기다리고, 1초 동안 유지
    }

    fun unlock(key: Long) {
        val lock = redissonClient.getLock("lock_key_$key")
        if (lock.isHeldByCurrentThread) {
            lock.unlock()
        }
    }
}
