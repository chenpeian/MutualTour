package cn.hfut.tour.util

import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.KNonNullExpression
import org.babyfish.jimmer.sql.kt.ast.expression.KNonNullPropExpression
import org.babyfish.jimmer.sql.kt.ast.expression.sql
import org.babyfish.jimmer.sql.kt.ast.mutation.KMutableDelete

/**
 * 在PostgresSQL中，判断参数数组是否为指定数组字段的子集
 */
infix fun <T> KNonNullPropExpression<List<T>>.arrayContains(array: Array<T>): KNonNullExpression<Boolean> {
    return sql(Boolean::class, "%e @> %v") {
        expression(this@arrayContains)
        value(array)
    }
}

inline fun <reified E : Any, ID : Any> KRepository<E, ID>.delete(noinline block: KMutableDelete<E>.() -> Unit): Int {
    return sql.createDelete(E::class, block).execute()
}