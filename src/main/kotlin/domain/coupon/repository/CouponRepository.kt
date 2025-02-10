package org.example.domain.coupon.repository

import jakarta.persistence.LockModeType
import org.example.domain.coupon.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CouponRepository : JpaRepository<Coupon, Long> {
    override fun findById(id: Long): Optional<Coupon>
    fun existsByValue(value: String): Boolean
}
