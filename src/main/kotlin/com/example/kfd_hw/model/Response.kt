package com.example.kfd_hw.model

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

interface Response {
    val status: HttpStatus
    val message: String

    fun asResponse() =
        ResponseEntity.status(status).body(this)
}