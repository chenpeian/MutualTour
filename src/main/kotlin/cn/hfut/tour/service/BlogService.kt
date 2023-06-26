package cn.hfut.tour.service

import cn.hfut.tour.http.BusinessError
import cn.hfut.tour.http.ResponseCode
import cn.hfut.tour.model.*
import cn.hfut.tour.model.vo.StatusVO
import cn.hfut.tour.util.arrayContains
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.spring.repository.orderBy
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.like
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*
import kotlin.io.path.*

interface BlogService : KRepository<Blog, UUID> {

    suspend fun insertBlog(
        uploaderId: Long,
        content: String,
        images: List<MultipartFile>,
        tags: List<String>,
        type: BlogType
    ) {
        withContext(Dispatchers.IO) {
            val imageIds = saveImages(images)
            insert(new(Blog::class).by {
                this.content = content
                uploader().apply {
                    id = uploaderId
                }
                this.tags = tags
                this.images = imageIds
                this.type = type
                updatedAt = LocalDateTime.now()
            })
        }
    }

    suspend fun findBlogs(
        userIds: List<Long>?,
        tags: List<String>?,
        content: String?,
        pageable: Pageable,
        type: BlogType?
    ): Page<Blog> = withContext(Dispatchers.IO) {
        pager(pageable).execute(
            sql.createQuery(Blog::class) {
                userIds?.takeIf { it.isNotEmpty() }?.let {
                    where(table.uploader.id valueIn it)
                }
                tags?.takeIf { it.isNotEmpty() }?.let {
                    where(table.tags arrayContains it.toTypedArray())
                }
                content?.takeIf { it.isNotEmpty() }?.let {
                    where(table.content like it)
                }
                type?.let {
                    where(table.type eq it)
                }
                orderBy(pageable.sort)
                select(table.fetch(DETAILED_BLOG_FETCHER))
            }
        )
    }

    suspend fun starBlog(performUserId: Long, blogId: UUID): StatusVO {
        val starers = findNullable(blogId, STATUS_BLOG_FETCHER)?.starers?.toMutableList()
            ?: throw BusinessError(ResponseCode.NOT_FOUND)
        val starred = !starers.removeIf { it.id == performUserId }
        if (starred) {
            starers.add(new(User::class).by {
                id = performUserId
            })
        }

        update(new(Blog::class).by {
            id = blogId
            starers()
            starers.forEach {
                starers().addBy { id = it.id }
            }
        }).let {
            return StatusVO(starred, findNullable(it.id, STATUS_BLOG_FETCHER)!!.starers.size)
        }
    }

    suspend fun likeBlog(performUserId: Long, blogId: UUID): StatusVO {
        val likers = findNullable(blogId, STATUS_BLOG_FETCHER)?.likers?.toMutableList()
            ?: throw BusinessError(ResponseCode.NOT_FOUND)
        val liked = !likers.removeIf { it.id == performUserId }
        if (liked) {
            likers.add(new(User::class).by {
                id = performUserId
            })
        }

        update(new(Blog::class).by {
            id = blogId
            likers()
            likers.forEach {
                likers().addBy { id = it.id }
            }
        }).let {
            return StatusVO(liked, findNullable(it.id, STATUS_BLOG_FETCHER)!!.likers.size)
        }
    }

    suspend fun updateBlog(
        uploaderId: Long,
        blogId: UUID,
        content: String,
        images: List<MultipartFile>,
        tags: List<String>
    ): Blog {
        val blog = findNullable(blogId, BRIEF_BLOG_FETCHER)
            ?.takeIf { it.uploader.id == uploaderId }
            ?: throw BusinessError(ResponseCode.NOT_FOUND)

        return withContext(Dispatchers.IO) {
            blog.images.forEach {
                (blogImagePath / it).deleteIfExists()
            }
            val imageIds = saveImages(images)
            update(new(Blog::class).by {
                id = blogId
                this.content = content
                this.images = imageIds
                this.tags = tags
                updatedAt = LocalDateTime.now()
            })
            findNullable(blogId, DETAILED_BLOG_FETCHER)!!
        }
    }

    /**
     * @return 保存的文件名列表
     */
    fun saveImages(images: List<MultipartFile>): List<String> =
        mutableListOf<String>().apply {
            images.forEach { image ->
                if (image.isEmpty) {
                    return@forEach
                }
                blogImagePath
                    .createDirectories()
                    .resolve("${UUID.randomUUID()}.jpg".also { add(it) })
                    .apply { createFile() }
                    .also { image.transferTo(it) }
            }
        }

    companion object {
        val blogImagePath = Path("static") / "images"
    }
}
