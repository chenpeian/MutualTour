package cn.hfut.tour.http

import org.springframework.http.HttpStatus

enum class ResponseCode(val httpStatus: HttpStatus, val code: Int, val message: String) {
    INVALID_USERNAME_OR_PASSWORD(HttpStatus.BAD_REQUEST, 100, "用户名或密码错误"),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, 101, "用户名已存在"),
    INVALID_USER_ID(HttpStatus.FORBIDDEN, 103, "无效的用户ID"),
    WRONG_OLD_PASSWORD(HttpStatus.BAD_REQUEST, 104, "原密码错误"),
    LOGIN_NEEDED(HttpStatus.FORBIDDEN, 105, "需要登录"),
    NOT_FOUND(HttpStatus.NOT_FOUND, 304, "未找到相应资源"),

    UNKNOWN_EXCEPTION(HttpStatus.BAD_REQUEST, 999, "未知异常")
}