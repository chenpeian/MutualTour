package cn.hfut.tour.config

import cn.hfut.tour.model.Blog
import org.babyfish.jimmer.meta.ImmutableProp
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.sql.runtime.DefaultDatabaseNamingStrategy
import org.babyfish.jimmer.sql.runtime.Reader
import org.babyfish.jimmer.sql.runtime.ScalarProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.Array
import java.sql.JDBCType
import javax.sql.DataSource

@Configuration
class JimmerConfig {

    @Bean
    fun databaseNamingStrategy(): DefaultDatabaseNamingStrategy =
        DefaultDatabaseNamingStrategy.LOWER_CASE

    /**
     * 为Jimmer框架提供 [kotlin.collections.List] 与 [java.sql.Array] 两种类型的转换器
     */
    @Suppress("UNCHECKED_CAST")
    @Bean
    fun varcharArrayScalarProvider(datasource: DataSource) = object : ScalarProvider<List<String>, Array>() {
        override fun toScalar(sqlValue: Array): List<String> {
            return (sqlValue.array as kotlin.Array<String>).toList()
        }

        override fun toSql(scalarValue: List<String>): Array {
            return datasource.connection.createArrayOf(JDBCType.VARCHAR.name, scalarValue.toTypedArray())
        }

        override fun reader() = Reader { rs, col ->
            return@Reader rs.getArray(col.get())
        }

        override fun getHandledProps(): MutableCollection<ImmutableProp> =
            mutableListOf(
                ImmutableType.get(Blog::class.java).getProp("tags"),
                ImmutableType.get(Blog::class.java).getProp("images")
            )

    }
}