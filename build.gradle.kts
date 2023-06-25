import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"
}

group = "cn.hfut"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main")
    }
}

dependencies {
    // spring boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    // jackson json 序列化
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // kotlin 反射（spring 必需）
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // kotlin 协程（spring 异步接口）
    val kotlinxCoroutinesVersion = "1.7.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk9:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
    // sa-token 安全认证框架
    implementation("cn.dev33:sa-token-spring-boot3-starter:1.34.0")
    // jimmer 持久层框架
    implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:0.7.84")
    ksp("org.babyfish.jimmer:jimmer-ksp:0.7.84")
    // postgresql 数据库驱动
    runtimeOnly("org.postgresql:postgresql")
    // spring boot test 测试集成框架
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
