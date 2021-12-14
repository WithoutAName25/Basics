plugins {
    kotlin("jvm") version "1.6.10"
    `java-library`
    `maven-publish`
    jacoco
}

group = "eu.withoutaname.lib"
version = "1.0.0-alpha.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    
    val junit = "5.8.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        csv.required.set(false)
        html.required.set(false)
        xml.required.set(true)
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                licenses {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
        }
    }
    repositories {
        maven {
            url = uri(
                    "https://withoutaname.eu/maven/${
                        if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"
                    }"
            )
            credentials {
                username = System.getenv("MAVEN_NAME") ?: ""
                password = System.getenv("MAVEN_TOKEN") ?: ""
            }
        }
    }
}