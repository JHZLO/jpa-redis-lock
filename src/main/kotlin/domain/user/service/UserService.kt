package org.example.domain.user.service

import jakarta.transaction.Transactional
import org.example.domain.coupon.entity.Coupon
import org.example.domain.coupon.repository.CouponRepository
import org.example.domain.coupon.service.CouponService
import org.example.domain.member.repository.UserRepository
import org.example.domain.user.dto.RegisterRequest
import org.example.domain.user.entity.User
import org.example.domain.user.repository.RedisLockRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val couponRepository: CouponRepository,
    private val couponService: CouponService,
    private val passwordEncoder: PasswordEncoder,
    private val redisLockRepository: RedisLockRepository
) {
    fun register(request: RegisterRequest): User {
        val user = User().apply {
            email = request.email
            password = passwordEncoder.encode(request.password)
        }
        userRepository.save(user)
        return user
    }

    fun login(email: String, rawPassword: String): User? {
        val user = userRepository.findByEmail(email) ?: return null
        return if (passwordEncoder.matches(rawPassword, user.password)) user else null
    }

    @Transactional
    fun applyCoupon(user: User): Coupon {
        user.coupon?.let { return it }
        val key: Long = user.id
        if (!redisLockRepository.lock(key)) {
            throw IllegalStateException("쿠폰 발급 중 잠금 획득에 실패했습니다.")
        }

        try {
            val coupon = couponService.createCoupon()
            if (coupon.id > 100) {
                couponRepository.delete(coupon)
                throw IllegalArgumentException("쿠폰 ID가 100을 초과했습니다. 발급이 중단됩니다.")
            }
            user.coupon = coupon
            userRepository.save(user)
            return coupon;
        } finally {
            redisLockRepository.unlock(key)
        }
    }
}
