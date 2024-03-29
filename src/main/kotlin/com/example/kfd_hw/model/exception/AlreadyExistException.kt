package com.example.kfd_hw.model.exception

import org.springframework.http.HttpStatus

class AlreadyExistException : AbstractException() {
    override val message: String
        get() = "Already exist"
    override val status = HttpStatus.BAD_REQUEST
}