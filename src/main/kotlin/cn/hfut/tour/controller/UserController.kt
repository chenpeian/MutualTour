package cn.hfut.tour.controller

import cn.dev33.satoken.annotation.SaCheckLogin
import cn.dev33.satoken.secure.BCrypt
import cn.dev33.satoken.stp.StpUtil
import cn.hfut.tour.http.BusinessError
import cn.hfut.tour.http.ResponseCode
import cn.hfut.tour.model.DETAILED_USER_FETCHER
import cn.hfut.tour.model.Gender
import cn.hfut.tour.model.User
import cn.hfut.tour.model.by
import cn.hfut.tour.model.vo.StatusVO
import cn.hfut.tour.model.vo.UserModifyPasswordVO
import cn.hfut.tour.model.vo.UserPasswordVO
import cn.hfut.tour.service.UserService
import org.babyfish.jimmer.kt.new
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/login")
    suspend fun login(@RequestBody vo: UserPasswordVO) {
        StpUtil.login(userService.validLogin(vo.username, vo.password))
    }

    @PostMapping("/register")
    suspend fun register(@RequestBody vo: UserPasswordVO) {
        userService.register(vo.username, vo.password)
    }

    @SaCheckLogin
    @PutMapping("/password")
    suspend fun modifyPassword(@RequestBody vo: UserModifyPasswordVO) {
        userService.findNullable(StpUtil.getLoginIdAsLong())
            ?.also {
                if (!BCrypt.checkpw(
                        vo.oldPassword,
                        it.password
                    )
                ) throw BusinessError(ResponseCode.WRONG_OLD_PASSWORD)
            }
            ?.let {
                userService.update(new(User::class).by {
                    username = it.username
                    password = BCrypt.hashpw(vo.newPassword)
                })
            } ?: throw BusinessError(ResponseCode.INVALID_USER_ID)
    }

    @SaCheckLogin
    @PutMapping
    suspend fun modifyDetailedInfo(
        username: String?,
        avatar: MultipartFile?,
        gender: Gender?,
        age: Short?
    ) {
        userService.findNullable(StpUtil.getLoginIdAsLong())
            ?.let {
                userService.saveUserInfo(it.id, username, avatar, gender, age)
            } ?: throw BusinessError(ResponseCode.INVALID_USER_ID)
    }

    @GetMapping("/", "/{userId}")
    suspend fun getSpecificUserDetailedInfo(@PathVariable(required = false) userId: Long?): User {
        return userService.findNullable(userId ?: StpUtil.getLoginIdAsLong(), DETAILED_USER_FETCHER)
            ?: throw BusinessError(ResponseCode.INVALID_USER_ID)
    }

    @PostMapping("/follow/{userId}")
    suspend fun followUser(@PathVariable("userId") targetUserId: Long): StatusVO {
        userService.findNullable(StpUtil.getLoginIdAsLong())
            ?.let {
                return userService.followUser(it.id, targetUserId)
            } ?: throw BusinessError(ResponseCode.INVALID_USER_ID)
    }

}