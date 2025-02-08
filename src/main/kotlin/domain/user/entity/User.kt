package org.example.domain.member.entity

import jakarta.persistence.*
import org.example.domain.coupon.entity.Coupon

@Entity
@Table(name = "User")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "email", unique = true)
    var email: String = ""

    @Column(name = "password")
    var password: String = ""

    @OneToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    var coupon: Coupon? = null
}
