package org.example.domain.user.service

import org.example.domain.member.entity.User
import org.example.domain.member.repository.UserRepository
import org.example.domain.user.dto.LoginRequest
import org.example.domain.user.dto.RegisterRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder

class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(request: RegisterRequest){
        val user = User().apply {
            email = request.email
            password = passwordEncoder.encode(request.password)
        }
        userRepository.save(user)
    }

    fun login(email: String, rawPassword: String):User? {
        val user = userRepository.findByEmail(email) ?: return null
        return if (passwordEncoder.matches(rawPassword, user.password)) user else null
    }
}
