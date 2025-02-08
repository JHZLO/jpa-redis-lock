package org.example.domain.coupon.service

import jakarta.transaction.Transactional
import org.example.domain.coupon.entity.Coupon
import org.example.domain.coupon.repository.CouponRepository
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository
) {

    companion object {
        const val CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        const val COUPON_LENGTH = 10
    }

    @Transactional
    fun createCoupon(): Coupon {
        var couponValue: String
        do {
            couponValue = generateCouponValue()
        } while (isDuplicatedCoupon(couponValue))

        val coupon = Coupon(value = couponValue)
        return couponRepository.save(coupon)
    }

    private fun generateCouponValue(): String {
        return (1..COUPON_LENGTH)
            .map { CHARSET.random() }
            .joinToString("")
    }

    private fun isDuplicatedCoupon(couponValue: String): Boolean {
        return couponRepository.existsByValue(couponValue)
    }
}
