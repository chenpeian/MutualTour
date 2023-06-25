package cn.hfut.tour.service

import cn.dev33.satoken.secure.BCrypt
import cn.hfut.tour.http.BusinessError
import cn.hfut.tour.http.ResponseCode
import cn.hfut.tour.model.*
import cn.hfut.tour.model.vo.StatusVO
import cn.hfut.tour.util.tryDBExecuting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.springframework.web.multipart.MultipartFile
import kotlin.io.path.*

interface UserService : KRepository<User, Long> {
    suspend fun register(
        username: String,
        password: String,
    ) {
        tryDBExecuting {
            insert(new(User::class).by {
                this.username = username
                this.password = BCrypt.hashpw(password)
            })
        }
    }

    suspend fun validLogin(
        username: String,
        password: String,
    ): Long {
        return findByUsername(username)?.let {
            if (BCrypt.checkpw(password, it.password))
                it.id
            else
                null
        } ?: throw BusinessError(ResponseCode.INVALID_USERNAME_OR_PASSWORD)
    }

    suspend fun saveUserInfo(
        userId: Long,
        username: String? = null,
        avatar: MultipartFile? = null,
        gender: Gender? = null,
        age: Short? = null,
    ) {
        avatar?.let {
            withContext(Dispatchers.IO) {
                (Path("static") / "avatar")
                    .createDirectories()
                    .resolve("$userId.jpg")
                    .apply { if (notExists()) createFile() }
                    .also { avatar.transferTo(it) }
            }
        }
        tryDBExecuting {
            update(new(User::class).by {
                id = userId
                username?.let {
                    this.username = it
                }
                gender?.let {
                    this.gender = gender
                }
                age?.let {
                    this.age = it
                }
            })
        }
    }

    suspend fun followUser(performUserId: Long, targetUserId: Long): StatusVO {
        val followings = findNullable(performUserId, FOLLOWING_USER_FETCHER)?.following?.toMutableList()
            ?: throw BusinessError(ResponseCode.INVALID_USER_ID)
        val followed = !followings.removeIf { it.id == targetUserId }
        if (followed) {
            followings.add(new(User::class).by {
                id = targetUserId
            })
        }

        update(new(User::class).by {
            id = performUserId
            following()
            followings.forEach {
                following().addBy { id = it.id }
            }
        }).let {
            return StatusVO(followed, findNullable(it.id, FOLLOWING_USER_FETCHER)!!.following.size)
        }
    }


    fun findByUsername(username: String, fetcher: Fetcher<User>? = null): User?
}