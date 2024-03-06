package com.example.kfd_hw.model.exception

import com.example.kfd_hw.model.Response
import com.fasterxml.jackson.annotation.JsonIncludeProperties
import org.springframework.http.HttpStatus

@JsonIncludeProperties("status", "message")
abstract class AbstractException : Response, Exception() {
    override val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    override val message: String
        get() = localizedMessage

}