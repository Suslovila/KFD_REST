package com.example.kfd_hw.controller

import com.example.kfd_hw.model.message.user.UserResponse
import java.util.UUID

interface IUserController {
    fun getAll(): Iterable<UserResponse>

    fun getById(id: UUID): UserResponse
}