package com.example.kfd_hw.model.exception

import org.springframework.http.HttpStatus

class NotFoundException : AbstractException() {
    override val message: String
        get() = "Not found"
    override val status = HttpStatus.NOT_FOUND
}