package com.example.kfd_hw.service

import com.example.kfd_hw.dto.authentication.AuthenticationRequest
import com.example.kfd_hw.dto.authentication.AuthenticationResponse
import com.example.kfd_hw.dto.authentication.RegistrationRequest

interface IAuthenticationService {
    fun login(authRequest: AuthenticationRequest): AuthenticationResponse
    fun registration(registrationRequest: RegistrationRequest): AuthenticationResponse
    fun refreshAccessToken(token: String): AuthenticationResponse
}