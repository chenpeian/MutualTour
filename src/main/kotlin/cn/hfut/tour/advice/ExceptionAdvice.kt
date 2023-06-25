package cn.hfut.tour.advice

import cn.dev33.satoken.exception.NotLoginException
import cn.hfut.tour.http.BusinessError
import cn.hfut.tour.http.BusinessErrorBody
import cn.hfut.tour.http.ResponseCode
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionAdvice {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BusinessError::class)
    fun onBusinessError(e: BusinessError): ResponseEntity<BusinessErrorBody> {
        e.cause?.let { log.error("unknown error", it) }
        return e.toResponseEntity()
    }

    @ExceptionHandler(NotLoginException::class)
    fun onNotLoginException(e: NotLoginException): ResponseEntity<BusinessErrorBody> {
        return BusinessError(ResponseCode.LOGIN_NEEDED).toResponseEntity()
    }
}