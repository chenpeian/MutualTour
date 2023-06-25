package cn.hfut.tour.config

import cn.dev33.satoken.context.SaHolder
import cn.dev33.satoken.filter.SaServletFilter
import cn.dev33.satoken.interceptor.SaInterceptor
import cn.dev33.satoken.router.SaHttpMethod
import cn.dev33.satoken.router.SaRouter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 注册Sa-Token的注解鉴权器
        registry.addInterceptor(SaInterceptor()).addPathPatterns("/**")
    }

    @Bean
    fun saServletFilter() = SaServletFilter().apply {
        addInclude("/**").addExclude("/favicon.ico").addExclude("/static/**")
        setBeforeAuth {
            SaHolder.getResponse().run {
                setHeader("Access-Control-Allow-Credentials", "true")
                setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                setHeader("Access-Control-Allow-Headers", "Content-Type, Cookies")
                setHeader("Access-Control-Allow-Origin", SaHolder.getRequest().getHeader("Origin"))
                setHeader("Access-Control-Max-Age", "3600")
            }
            SaRouter.match(SaHttpMethod.OPTIONS)
                .free {  }
                .back()
        }
    }
}
