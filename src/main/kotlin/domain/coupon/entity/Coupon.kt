package org.example.domain.coupon.entity

import jakarta.persistence.*

@Entity
@Table(name = "coupon")
class Coupon(value: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "value", unique = true)
    var value: String = ""
}
