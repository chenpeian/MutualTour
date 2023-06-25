package cn.hfut.tour.config

import cn.hfut.tour.model.BlogType
import cn.hfut.tour.model.Gender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter

@Configuration
class SerializeConfig {

    /**
     * 为Spring提供枚举类型的数字转换器
     */
    @Bean
    fun genderConverter() = object : Converter<String, Gender> {
        override fun convert(source: String): Gender? = source.toIntOrNull()?.let { Gender.values()[it] }
    }

    @Bean
    fun blogTypeConverter() = object : Converter<String, BlogType> {
        override fun convert(source: String): BlogType? = source.toIntOrNull()?.let { BlogType.values()[it] }
    }
}