package cn.hfut.tour.util

import cn.hfut.tour.http.BusinessError
import cn.hfut.tour.http.ResponseCode
import org.babyfish.jimmer.sql.runtime.ExecutionException
import java.sql.SQLException

enum class PSQLErrorCodes(val code: String) {
    UNIQUE_VIOLATION("23505"),
    UNKNOWN("");

    companion object {
        fun fromCode(code: String): PSQLErrorCodes =
            values().find { it.code == code } ?: UNKNOWN
    }
}

inline val ExecutionException.code: PSQLErrorCodes
    get() = PSQLErrorCodes.fromCode((this.cause as? SQLException)?.sqlState ?: "")

/**
 * 捕获 [block] 抛出的数据库异常并处理
 */
inline fun tryDBExecuting(block: () -> Unit) {
    try {
        block()
    } catch (e: ExecutionException) {
        when (e.code) {
            PSQLErrorCodes.UNIQUE_VIOLATION -> throw BusinessError(ResponseCode.USERNAME_ALREADY_EXISTS)
            else -> throw BusinessError(ResponseCode.UNKNOWN_EXCEPTION, cause = e)
        }
    }
}