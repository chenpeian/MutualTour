package cn.hfut.tour.service

import cn.hfut.tour.model.Comment
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.fetcher.Fetcher
import java.util.*

interface CommentService : KRepository<Comment, UUID> {
    fun findCommentsByBlogId(blogId: UUID, fetcher: Fetcher<Comment>? = null): List<Comment>
}