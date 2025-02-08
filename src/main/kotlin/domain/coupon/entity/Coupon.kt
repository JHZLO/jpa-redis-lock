package org.example.domain.coupon.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "coupon")
class Coupon {
    @Id
    val id: Long = 0L

    @Column(name = "value")
    var value: String? = null
}
