package org.example.domain.member.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.example.domain.coupon.entity.Coupon

@Entity
@Table(name = "member")
class Member {
    @Id
    val id: Long = 0L

    @Column(name = "name")
    var name: String? = null

    @OneToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    var coupon: Coupon? = null
}
