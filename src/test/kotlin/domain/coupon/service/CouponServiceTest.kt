package domain.coupon.service

import org.example.domain.coupon.entity.Coupon
import org.example.domain.coupon.repository.CouponRepository
import org.example.domain.coupon.service.CouponService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@ExtendWith(MockitoExtension::class)
class CouponServiceTest {

    @Mock
    private lateinit var couponRepository: CouponRepository

    @InjectMocks
    private lateinit var couponService: CouponService

    @Test
    fun 쿠폰을_성공적으로_발급() {
        // Given
        val coupon = Coupon(value = "A1B2C3D4E5")
        given(couponRepository.count()).willReturn(50L)
        given(couponRepository.existsByValue(anyString())).willReturn(false)
        given(couponRepository.save(any(Coupon::class.java))).willReturn(coupon)

        // When
        val createdCoupon = couponService.createCoupon()

        // Then
        assertNotNull(createdCoupon)
        assertTrue(createdCoupon.value.matches(Regex("^[A-Za-z0-9]{10}$")))
    }

    @Test
    fun 쿠폰_수량_한도_초과_시_예외_발생() {
        // Given
        given(couponRepository.count()).willReturn(100L)

        // When & Then
        assertThrows<IllegalArgumentException> {
            couponService.createCoupon()
        }
    }

    @Test
    fun ID로_쿠폰_조회_성공() {
        // Given
        val coupon = Coupon(value = "A1B2C3D4E5")
        given(couponRepository.findById(1L)).willReturn(Optional.of(coupon))

        // When
        val foundCoupon = couponService.findById(1L)

        // Then
        assertNotNull(foundCoupon)
        assertEquals("A1B2C3D4E5", foundCoupon.value)
    }

    @Test
    fun 존재하지_않는_쿠폰인_경우_id_조회_안됨() {
        // Given
        given(couponRepository.findById(1L)).willReturn(Optional.empty())

        // When & Then
        assertThrows<NoSuchElementException> {
            couponService.findById(1L)
        }
    }

    // TODO: 중복 쿠폰 검증 테스트 코드 작성
}
