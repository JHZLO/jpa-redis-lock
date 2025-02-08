package org.example.domain.member.repository

import org.example.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository: JpaRepository<Member, Long> {
    override fun findById(id: Long): Optional<Member>
}
