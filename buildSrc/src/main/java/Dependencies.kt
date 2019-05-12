object Versions {
    /**
     * General versions
     */
    const val COMPILE_SDK = 28
    const val TARGET_SDK = 28
    const val MIN_SDK = 21
    const val KOTLIN = "1.3.31"

    /**
     * Gradle dependencies versions
     */
    const val ANDROID_GRADLE = "3.3.1"

    /**
     * Support versions
     */
    const val KTX = "1.0.1"
    const val CONSTRAINT = "1.1.3"
    const val APP_COMPAT = "1.0.0"
    const val MATERIAL = "1.0.0"
    const val ARCH_COMPONENTS = "2.0.0"
    const val NAVIGATION = "2.0.0"

    /**
     * Thrid party versions
     */
    const val DAGGER = "2.19"
    const val RX_JAVA_2 = "2.1.10"
    const val RX_KOTLIN = "2.2.0"

    const val GLIDE = "4.6.1"
    const val MATERIAL_DIALOG = "2.8.1"
    const val TIMBER = "4.7.0"
    const val EXOPLAYER = "2.8.4"
}

object Libraries {
    /**
     * Basic dependencies
     */
    const val KTX = "androidx.core:core-ktx:${Versions.KTX}"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"

    /**
     * Support and basic android dependencies
     */
    @JvmField
    val SUPPORT = arrayOf(
        "androidx.appcompat:appcompat:${Versions.APP_COMPAT}",
        "com.google.android.material:material:${Versions.MATERIAL}",
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT}"
    )
    const val ARCH_COMPONENTS = "android.arch.lifecycle:extensions:${Versions.ARCH_COMPONENTS}"

    @JvmField
    val NAVIGATION = arrayOf(
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}",
        "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    )

    /**
     * Third party libraries dependencies
     */
    const val DAGGER = "com.google.dagger:dagger:${Versions.DAGGER}"
    const val DAGGER_PROCESSOR = "com.google.dagger:dagger-compiler:${Versions.DAGGER}"
    @JvmField
    val DAGGER_ANDROID = arrayOf(
        DAGGER,
        "com.google.dagger:dagger-android-support:${Versions.DAGGER}"
    )
    @JvmField
    val DAGGER_ANDROID_PROCESSORS = arrayOf(
        DAGGER_PROCESSOR,
        "com.google.dagger:dagger-android-processor:${Versions.DAGGER}"
    )

    const val RX_JAVA_2 = "io.reactivex.rxjava2:rxjava:${Versions.RX_JAVA_2}"
    const val RX_KOTLIN = "io.reactivex.rxjava2:rxkotlin:${Versions.RX_KOTLIN}"

    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val MATERIAL_DIALOG = "com.afollestad.material-dialogs:core:${Versions.MATERIAL_DIALOG}"

    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val EXOPLAYER = "com.google.android.exoplayer:exoplayer:${Versions.EXOPLAYER}"

}


object Plugins {
    const val ANDROID_GRADLE_PLUGIN = "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val SAFE_ARGS_PLUGIN = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"
}