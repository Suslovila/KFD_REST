package com.example.kfd_hw.service.impl

import com.example.kfd_hw.config.JwtProperties
import com.example.kfd_hw.database.dao.RefreshTokenDao
import com.example.kfd_hw.database.dao.UserDao
import com.example.kfd_hw.database.entity.RefreshTokenEntity
import com.example.kfd_hw.database.entity.UserEntity
import com.example.kfd_hw.dto.authentication.AuthenticationRequest
import com.example.kfd_hw.dto.authentication.AuthenticationResponse
import com.example.kfd_hw.dto.authentication.RegistrationRequest
import com.example.kfd_hw.model.exception.AlreadyExistException
import com.example.kfd_hw.model.exception.BadRequestException
import com.example.kfd_hw.model.exception.NotFoundException
import com.example.kfd_hw.service.IAuthenticationService
import com.example.kfd_hw.service.TokenService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationServiceImpl(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val userDao: UserDao,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenDao: RefreshTokenDao,
    private val encoder: PasswordEncoder,
) : IAuthenticationService {


    override fun login(authRequest: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )
        val user = userDetailsService.loadUserByUsername(authRequest.email)
        val refreshToken = generateRefreshToken(user)
        val accessToken = generateAccessToken(user)
        val oldRefresh = refreshTokenDao.findByEmail(user.username)
        oldRefresh!!.refreshToken = refreshToken
        refreshTokenDao.save(oldRefresh)
        return AuthenticationResponse(
            accessToken,
            refreshToken,
        )
    }

    override fun registration(registrationRequest: RegistrationRequest): AuthenticationResponse {
        val user = userDao.findByEmail(registrationRequest.email)
        if (user != null) {
            throw AlreadyExistException()
        }
        val userDetails = userDetailsService.createUserDetails(registrationRequest)
        val accessToken = generateAccessToken(userDetails)
        val refreshToken = generateRefreshToken(userDetails)
        userDao.save(createUser(registrationRequest))
        refreshTokenDao.save(createRefresh(refreshToken, registrationRequest.email))
        return AuthenticationResponse(
            accessToken,
            refreshToken,
        )
    }

    override fun refreshAccessToken(token: String): AuthenticationResponse {
        val extractedEmail = tokenService.extractEmail(token) ?: throw BadRequestException()
        val currentUserDetails = userDetailsService.loadUserByUsername(extractedEmail)
        val refreshTokenEmail = refreshTokenDao.findByRefreshToken(token) ?: throw NotFoundException()
        if (tokenService.isExpired(token) || currentUserDetails.username != refreshTokenEmail.email) {
            throw BadRequestException()
        }
        return AuthenticationResponse(
            generateAccessToken(currentUserDetails),
            refreshTokenEmail.refreshToken,
        )
    }


    private fun generateRefreshToken(user: UserDetails) = tokenService.generate(
        user,
        Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
    )

    private fun generateAccessToken(user: UserDetails) = tokenService.generate(
        user,
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
    )

    private fun createUser(data: RegistrationRequest) = UserEntity(
        data.username,
        data.email,
        encoder.encode(data.password),
    )

    private fun createRefresh(refreshToken: String, email: String) = RefreshTokenEntity(
        refreshToken,
        email,
    )
}