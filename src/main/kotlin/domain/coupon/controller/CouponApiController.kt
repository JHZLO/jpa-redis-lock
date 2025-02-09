package org.example.domain.coupon.controller

import org.example.domain.coupon.dto.CouponResponse
import org.example.domain.coupon.service.CouponService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/coupon/{id}")
    fun getCoupon(@PathVariable id: Long): ResponseEntity<CouponResponse> {
        val coupon = couponService.findById(id)
        val response = CouponResponse(id = coupon.id, value = coupon.value)

        return ResponseEntity.ok().body(response)
    }
}
