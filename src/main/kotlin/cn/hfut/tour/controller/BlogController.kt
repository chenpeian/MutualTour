package cn.hfut.tour.controller

import cn.dev33.satoken.annotation.SaCheckLogin
import cn.dev33.satoken.stp.StpUtil
import cn.hfut.tour.http.BusinessError
import cn.hfut.tour.http.ResponseCode
import cn.hfut.tour.model.*
import cn.hfut.tour.model.vo.StatusVO
import cn.hfut.tour.service.BlogService
import cn.hfut.tour.util.delete
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/blog")
class BlogController(
    private val blogService: BlogService
) {
    @SaCheckLogin
    @PostMapping
    suspend fun postBlog(
        @RequestParam content: String,
        @RequestParam(required = false) images: List<MultipartFile>?,
        @RequestParam(required = false) tags: List<String>?,
        @RequestParam type: BlogType
    ) {
        blogService.insertBlog(StpUtil.getLoginIdAsLong(), content, images ?: listOf(), tags ?: listOf(), type)
    }

    @GetMapping
    suspend fun getBlogs(
        @RequestParam(required = false) userIds: List<Long>?,
        @RequestParam(required = false) tags: List<String>?,
        @RequestParam pageSize: Int,
        @RequestParam pageCount: Int,
        @RequestParam(required = false, defaultValue = "updatedAt") orderBy: String,
        @RequestParam(required = false) content: String?,
        @RequestParam(required = false) type: BlogType?
    ): Page<Blog> {
        return blogService.findBlogs(
            userIds,
            tags,
            content,
            PageRequest.of(pageCount, pageSize, Sort.by(orderBy).descending()),
            type
        )
    }

    @SaCheckLogin
    @PostMapping("/{blogId}/star")
    suspend fun starBlog(@PathVariable blogId: UUID): StatusVO {
        return blogService.starBlog(StpUtil.getLoginIdAsLong(), blogId)
    }

    @SaCheckLogin
    @PostMapping("/{blogId}/like")
    suspend fun likeBlog(@PathVariable blogId: UUID): StatusVO {
        return blogService.likeBlog(StpUtil.getLoginIdAsLong(), blogId)
    }

    @SaCheckLogin
    @DeleteMapping("/{blogId}")
    suspend fun deleteBlog(@PathVariable blogId: UUID) {
        blogService.delete {
            where(
                table.id eq blogId,
                table.uploader.id eq StpUtil.getLoginIdAsLong()
            )
        }.takeIf { it == 1 }
            ?: throw BusinessError(ResponseCode.NOT_FOUND)
    }

    @SaCheckLogin
    @PutMapping("/{blogId}")
    suspend fun updateBlog(
        @PathVariable blogId: UUID,
        @RequestParam content: String,
        @RequestParam(required = false) images: List<MultipartFile>?,
        @RequestParam(required = false) tags: List<String>?
    ): Blog {
        return blogService.updateBlog(StpUtil.getLoginIdAsLong(), blogId, content, images ?: listOf(), tags ?: listOf())
    }
}
