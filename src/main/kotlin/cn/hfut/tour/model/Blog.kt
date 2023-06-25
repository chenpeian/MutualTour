package cn.hfut.tour.model

import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.time.LocalDateTime
import java.util.*

@Entity
interface Blog {
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    val id: UUID

    @ManyToOne
    val uploader: User
    val updatedAt: LocalDateTime
    val content: String
    val type: BlogType

    @OneToMany(mappedBy = "blog")
    val comments: List<Comment>

    val tags: List<String>
    val images: List<String>

    @ManyToMany(mappedBy = "liked")
    val likers: List<User>

    @ManyToMany(mappedBy = "starred")
    val starers: List<User>
}

@EnumType(EnumType.Strategy.ORDINAL)
enum class BlogType {
    QUESTION,
    FIND_FRIENDS,
    TOURISM_SHARING
}

val STATUS_BLOG_FETCHER = newFetcher(Blog::class).by {
    starers()
    likers()
}

val BRIEF_BLOG_FETCHER = newFetcher(Blog::class).by {
    allScalarFields()
    uploader()
    starers()
    comments()
    likers()
}

val DETAILED_BLOG_FETCHER = newFetcher(Blog::class).by {
    allScalarFields()
    starers(BRIEF_USER_FETCHER)
    likers(BRIEF_USER_FETCHER)
    comments(BRIEF_COMMENT_FETCHER)
    uploader(BRIEF_USER_FETCHER)
}