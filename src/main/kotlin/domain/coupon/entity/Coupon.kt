package org.example.domain.coupon.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "coupon")
class Coupon(
    @Column(name = "value", unique = true)
    var value: String = ""
): Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Version
    val version: Long? = null
}
