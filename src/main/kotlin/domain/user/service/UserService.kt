package org.example.domain.user.service

import jakarta.persistence.EntityManager
import jakarta.persistence.OptimisticLockException
import org.example.domain.coupon.entity.Coupon
import org.example.domain.coupon.repository.CouponRepository
import org.example.domain.coupon.service.CouponService
import org.example.domain.user.entity.User
import org.example.domain.member.repository.UserRepository
import org.example.domain.user.dto.RegisterRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val couponRepository: CouponRepository,
    private val couponService: CouponService,
    private val passwordEncoder: PasswordEncoder
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

    fun applyCoupon(user: User): Coupon {
        user.coupon?.let { return it }

        if (getCouponTotalCount() >= 100) {
            throw IllegalArgumentException("쿠폰 한도 수량 초과로 발급이 불가능합니다.")
        }

        var retryCount = 3
        while (retryCount > 0) {
            try {
                val coupon = couponService.createCoupon()  // 쿠폰 생성
                if (coupon.id > 100) {  // 쿠폰 ID가 100 이상이면 예외 발생
                    couponRepository.delete(coupon)
                    throw IllegalArgumentException("쿠폰 ID가 100을 초과했습니다. 발급이 중단됩니다.")
                }
                user.coupon = coupon
                userRepository.save(user)  // save 시 낙관적 락이 적용됨
                return coupon
            } catch (e: OptimisticLockException) {
                retryCount--
                if (retryCount == 0) {
                    throw e
                }
            }
        }
        throw IllegalStateException("쿠폰 발급 실패")
    }

    private fun getCouponTotalCount(): Long{
        return couponRepository.count();
    }
}
