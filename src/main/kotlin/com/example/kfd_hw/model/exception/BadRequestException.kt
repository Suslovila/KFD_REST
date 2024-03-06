package com.example.kfd_hw.model.exception

import org.springframework.http.HttpStatus

class BadRequestException : AbstractException() {
    override val message: String
        get() = "Bad Request"
    override val status = HttpStatus.BAD_REQUEST
}