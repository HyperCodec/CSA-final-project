plugins {
    id("java")
}


group = "finalproject"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.2")

    // might remove these since there aren't any tests
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.apache.commons:commons-lang3:3.0")
    implementation("org.json:json:20231013")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "finalproject.Game"
        )
    }
}

tasks.test {
    useJUnitPlatform()
}