package org.example.domain.coupon.controller

import org.example.domain.coupon.service.CouponService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Controller
class CouponApiController(
    private val couponService: CouponService
) {
    @PostMapping("/coupon/issue")
    fun issueCoupon(): ResponseEntity<String> {
        return ResponseEntity.ok("쿠폰이 발급되었습니다.")
    }
}
