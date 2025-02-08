package org.example.domain.member.entity

import jakarta.persistence.*
import org.example.domain.coupon.entity.Coupon

@Entity
@Table(name = "member")
class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "email", unique = true)
    val email: String = ""

    @Column(name = "password")
    var password: String = ""

    @OneToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    var coupon: Coupon? = null
}
