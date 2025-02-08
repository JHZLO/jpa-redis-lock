package org.example.domain.coupon.repository

import org.example.domain.coupon.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CouponRepository: JpaRepository<Coupon, Long> {
    override fun findById(id: Long): Optional<Coupon>
}
