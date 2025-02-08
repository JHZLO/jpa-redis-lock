package org.example.domain.coupon.controller

import org.example.domain.coupon.dto.CouponResponse
import org.example.domain.coupon.service.CouponService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponApiController(
    private val couponService: CouponService
) {
    @PostMapping("/coupon/issue")
    fun issueCoupon(): ResponseEntity<CouponResponse> {
        try {
            val coupon = couponService.createCoupon()
            val response = CouponResponse(id = coupon.id, value = coupon.value)
            return ResponseEntity.ok(response)
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }
    }
}
