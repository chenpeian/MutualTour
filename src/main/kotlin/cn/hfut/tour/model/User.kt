package cn.hfut.tour.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher

@Entity
@Table(name = "t_user")
interface User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long
    @Key
    val username: String
    @get:JsonIgnore
    val password: String
    val gender: Gender?
    val age: Short?

    @ManyToMany
    @JoinTable(
        name = "user_follower_mapping",
        joinColumnName = "follower_id",
        inverseJoinColumnName = "target_id"
    )
    val following: List<User>

    @ManyToMany(mappedBy = "following")
    val followers: List<User>

    @ManyToMany
    @JoinTable(name = "user_blog_liked_mapping")
    val liked: List<Blog>

    @ManyToMany
    @JoinTable(name = "user_blog_starred_mapping")
    val starred: List<Blog>
}

@EnumType(EnumType.Strategy.ORDINAL)
enum class Gender {
    FEMALE,
    MALE,
}

val FOLLOWING_USER_FETCHER = newFetcher(User::class).by {
    following()
}

val BRIEF_USER_FETCHER = newFetcher(User::class).by {
    allScalarFields()
    password(false)
    following()
    followers()
}

val DETAILED_USER_FETCHER = newFetcher(User::class).by {
    allScalarFields()
    password(false)
    following(BRIEF_USER_FETCHER)
    followers(BRIEF_USER_FETCHER)
    liked(BRIEF_BLOG_FETCHER)
    starred(BRIEF_BLOG_FETCHER)
}