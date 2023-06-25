package cn.hfut.tour.controller

import cn.dev33.satoken.annotation.SaCheckLogin
import cn.dev33.satoken.stp.StpUtil
import cn.hfut.tour.http.BusinessError
import cn.hfut.tour.http.ResponseCode
import cn.hfut.tour.model.*
import cn.hfut.tour.model.vo.CommentVO
import cn.hfut.tour.service.CommentService
import cn.hfut.tour.util.delete
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/blog/{blogId}/comment")
class CommentController(
    private val commentService: CommentService
) {

    @SaCheckLogin
    @PostMapping
    suspend fun leaveComment(@PathVariable blogId: UUID, @RequestBody vo: CommentVO): Comment {
        return commentService.insert(new(Comment::class).by {
            content = vo.content
            commenter().apply {
                id = StpUtil.getLoginIdAsLong()
            }
            blog().apply {
                id = blogId
            }
            createdAt = LocalDateTime.now()
        })
    }

    @SaCheckLogin
    @DeleteMapping("/{commentId}")
    suspend fun deleteComment(@PathVariable blogId: UUID, @PathVariable commentId: UUID): List<Comment> {
        commentService.delete {
            where(
                table.id eq commentId,
                table.blog.id eq blogId,
                StpUtil.getLoginIdAsLong().let {
                    or(
                        table.blog.uploader.id eq it,
                        table.commenter.id eq it
                    )
                }
            )
        }.takeIf { it == 1 }
            ?: throw BusinessError(ResponseCode.NOT_FOUND)
        return commentService.findCommentsByBlogId(blogId, BRIEF_COMMENT_FETCHER)
    }
}