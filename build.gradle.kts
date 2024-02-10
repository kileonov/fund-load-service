import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "com.kleonov"
version = "0.0.1-SNAPSHOT"

val exposedVersion = "0.47.0"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("com.h2database:h2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")
	implementation("org.flywaydb:flyway-core")

	implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

	implementation("org.jetbrains.exposed:exposed-jodatime:$exposedVersion")
	// or
	implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
	// or
	implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")

	implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
