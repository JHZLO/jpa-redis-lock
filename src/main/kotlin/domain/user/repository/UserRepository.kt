package org.example.domain.member.repository

import org.example.domain.member.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Long> {
    override fun findById(id: Long): Optional<User>
}
