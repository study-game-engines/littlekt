import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("module.publication")
}

kotlin {
    tasks.withType<Test> {
        var env = project.properties["env"] as? String
        if (env == null) {
            env = System.getProperty("env")
        }
        systemProperty("env", env ?: "dev")
        jvmArgs("--enable-preview")
    }
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
            compileJavaTaskProvider?.get()?.options?.compilerArgs?.add("--enable-preview")
        }
    }
    js(KotlinJsCompilerType.IR) {
        browser {
            binaries.executable()
            testTask { useKarma { useChromeHeadless() } }
        }

        this.attributes.attribute(KotlinPlatformType.attribute, KotlinPlatformType.js)

        compilations.all { kotlinOptions.sourceMap = true }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting { dependencies { implementation(libs.kotlin.test) } }

        //noinspection UseTomlInstead
        val jvmMain by getting {
            dependencies {
                implementation(project(":wgpu-ffm"))
                implementation(project(":wgpu-natives"))

                implementation(libs.mp3.decoder)

                implementation(libs.lwjgl.core)
                implementation(libs.lwjgl.glfw)
                implementation(libs.lwjgl.openal)
                implementation(libs.lwjgl.stb)

                listOf(
                        "natives-windows",
                        "natives-windows-arm64",
                        "natives-linux",
                        "natives-linux-arm64",
                        "natives-macos",
                        "natives-macos-arm64"
                    )
                    .forEach { platform ->
                        runtimeOnly("${libs.lwjgl.core.get()}:$platform")
                        runtimeOnly("${libs.lwjgl.glfw.get()}:$platform")
                        runtimeOnly("${libs.lwjgl.openal.get()}:$platform")
                        runtimeOnly("${libs.lwjgl.stb.get()}:$platform")
                    }
            }
        }
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting

        val jvmAndroidMain = maybeCreate("jvmAndroidMain")

        jvmAndroidMain.dependsOn(commonMain)
        jvmMain.dependsOn(jvmAndroidMain)
        jvmTest.dependsOn(commonTest)
        jsTest.dependsOn(commonTest)

        all {
            languageSettings.apply {
                progressiveMode = true
                optIn("kotlin.contracts.ExperimentalContracts")
                optIn("kotlin.time.ExperimentalTime")
            }
        }

        targets.configureEach {
            compilations.configureEach {
                compilerOptions.configure { freeCompilerArgs.add("-Xexpect-actual-classes") }
            }
        }
    }
}
