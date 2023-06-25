package cn.hfut.tour.http

import org.springframework.http.ResponseEntity

class BusinessError(
    val code: ResponseCode,
    message: String = code.message,
    cause: Throwable? = null
): Exception(message, cause) {
    // 禁用本类型的错误栈
    override fun fillInStackTrace(): Throwable = this

    fun toResponseEntity(): ResponseEntity<BusinessErrorBody> {
        return ResponseEntity
            .status(code.httpStatus)
            .body(BusinessErrorBody(code.code, message!!))
    }
}

data class BusinessErrorBody(val code: Int, val message: String)