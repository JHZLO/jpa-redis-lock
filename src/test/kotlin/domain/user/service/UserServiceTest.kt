package org.example.domain.user.service

import org.example.domain.coupon.entity.Coupon
import org.example.domain.coupon.repository.CouponRepository
import org.example.domain.coupon.service.CouponService
import org.example.domain.user.entity.User
import org.example.domain.member.repository.UserRepository
import org.example.domain.user.dto.RegisterRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var couponService: CouponService

    @Mock
    private lateinit var couponRepository: CouponRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun 회원가입_성공() {
        // Given
        val request = RegisterRequest(email = "test@example.com", password = "password123")
        val encodedPassword = "encoded_password123"
        val user = User().apply {
            email = request.email
            password = encodedPassword
        }

        given(passwordEncoder.encode(request.password)).willReturn(encodedPassword)
        given(userRepository.save(any(User::class.java))).willReturn(user)

        // When
        val registeredUser = userService.register(request)

        // Then
        assertNotNull(registeredUser)
        assertEquals(request.email, registeredUser.email)
        assertEquals(encodedPassword, registeredUser.password)
    }

    @Test
    fun 로그인_성공() {
        // Given
        val email = "test@example.com"
        val rawPassword = "password123"
        val encodedPassword = "encoded_password123"
        val user = User().apply {
            this.email = email
            this.password = encodedPassword
        }

        given(userRepository.findByEmail(email)).willReturn(user)
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true)

        // When
        val loggedInUser = userService.login(email, rawPassword)

        // Then
        assertNotNull(loggedInUser)
        assertEquals(email, loggedInUser!!.email)
    }

    @Test
    fun 로그인_실패_비밀번호_불일치() {
        // Given
        val email = "test@example.com"
        val rawPassword = "wrong_password"
        val encodedPassword = "encoded_password123"
        val user = User().apply {
            this.email = email
            this.password = encodedPassword
        }

        given(userRepository.findByEmail(email)).willReturn(user)
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(false)

        // When
        val loggedInUser = userService.login(email, rawPassword)

        // Then
        assertNull(loggedInUser)
    }

    @Test
    fun 쿠폰_신청_성공() {
        // Given
        val user = User().apply {
            email = "test@example.com"
        }
        val coupon = Coupon(value = "A1B2C3D4E5")

        given(couponService.createCoupon()).willReturn(coupon)
        given(userRepository.save(user)).willReturn(user)

        // When
        val appliedCoupon = userService.applyCoupon(user)

        // Then
        assertNotNull(appliedCoupon)
        assertEquals("A1B2C3D4E5", appliedCoupon.value)
        assertEquals(coupon, user.coupon)
    }

    @Test
    fun 이미_쿠폰이_있는_경우_기존_쿠폰_반환() {
        // Given
        val existingCoupon = Coupon(value = "A1B2C3D4E5")
        val user = User().apply {
            email = "test@example.com"
            coupon = existingCoupon
        }

        // When
        val appliedCoupon = userService.applyCoupon(user)

        // Then
        assertNotNull(appliedCoupon)
        assertEquals(existingCoupon, appliedCoupon)
    }
}
