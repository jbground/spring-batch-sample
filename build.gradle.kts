plugins {
    id("java")
    id("idea")
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

springBoot{
    mainClass.value("com.jbground.batch.BatchApplication")
}

group = "com.jbground.batch"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch:2.7.2");
    implementation("org.springframework.batch:spring-batch-integration:4.3.8");
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:2.7.2");


    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")


}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName("jar"){

}

tasks.getByName("bootJar"){
    println("Spring batch + boot")
    setProperty("archiveFileName", "jbground.jar")
    version = "1.0.0"
}
