package cn.hfut.tour.model.vo

data class UserPasswordVO(
    val username: String,
    val password: String
)

data class UserModifyPasswordVO(
    val oldPassword: String,
    val newPassword: String
)