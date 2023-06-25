package cn.hfut.tour.model

import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.time.LocalDateTime
import java.util.UUID

@Entity
interface Comment {
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    val id: UUID
    val content: String
    @ManyToOne
    @Key
    @OnDissociate(DissociateAction.DELETE)
    val commenter: User
    @ManyToOne
    @Key
    @OnDissociate(DissociateAction.DELETE)
    val blog: Blog
    val createdAt: LocalDateTime
}

val BRIEF_COMMENT_FETCHER = newFetcher(Comment::class).by {
    allScalarFields()
    blog()
    commenter(BRIEF_USER_FETCHER)
}