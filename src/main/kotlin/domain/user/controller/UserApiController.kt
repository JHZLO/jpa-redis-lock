package org.example.domain.user.controller

import jakarta.servlet.http.HttpSession
import org.example.domain.coupon.dto.CouponResponse
import org.example.domain.user.dto.LoginRequest
import org.example.domain.user.dto.RegisterRequest
import org.example.domain.user.dto.UserResponse
import org.example.domain.user.entity.User
import org.example.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserApiController(
    private val userService: UserService
) {
    @PostMapping("/auth/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
        val user = userService.register(request)
        val response = UserResponse(email = user.email, password = user.password)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest, session: HttpSession): ResponseEntity<String> {
        val user = userService.login(request.email, request.password)
        return if (user != null) {
            session.setAttribute("user", user)
            ResponseEntity.ok("로그인 성공")
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보 유효하지 않음")
        }
    }

    @PostMapping("/apply/coupon")
    fun applyCoupon(session: HttpSession): ResponseEntity<CouponResponse> {
        try {
            val user = session.getAttribute("user") as? User
                ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

            val coupon = userService.applyCoupon(user)
            val response = CouponResponse(id = coupon.id, value = coupon.value)
            return ResponseEntity.ok(response)
        } catch (e: Exception){
            return ResponseEntity.status(404).build()
        }
    }
}
