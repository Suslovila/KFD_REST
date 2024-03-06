package com.example.kfd_hw.controller

import com.example.kfd_hw.model.exception.AbstractException
import com.example.kfd_hw.model.exception.ApiException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionResolver {
    @ExceptionHandler(Exception::class)
    fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: Exception,
    ) {
        val objectMapper = ObjectMapper().findAndRegisterModules()
        val wrappedException = wrapExceptionToApiError(exception)
        response.contentType = MediaType.APPLICATION_JSON.toString()
        response.status = wrappedException.status.value()
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(wrappedException))
    }

    private fun wrapExceptionToApiError(exception: Exception): AbstractException {
        if (exception is AbstractException) return exception
        if (exception is MethodArgumentNotValidException) {
            var message = ""
            for (error in exception.fieldErrors) {
                message += "${error.field}: ${error.defaultMessage};\n"
            }
            return ApiException(HttpStatus.BAD_REQUEST, message)
        }
        return ApiException(message = exception.message.orEmpty())
    }

}