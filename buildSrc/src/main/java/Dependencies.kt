object PlatformDeps {
    val minSdk = 19
    val targetSdk = 26
    val compileSdk = 27
    val buildTools = "27.0.3"
    val androidPlugin = "3.0.1"
    val kotlinVersion = "1.2.0"
    val appBaseId = "com.rixspi.simplecompass"

    val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    val kotlin_android_ext = "org.jetbrains.kotlin:kotlin-android-extensions:$kotlinVersion"
    val gradle_build = "com.android.tools.build:gradle:$androidPlugin"
    val kotlinSdk = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
}

object Views {
    private val constraintLayoutVersion = "1.1.0-beta3"

    val constraintLayout = "com.android.support.constraint:constraint-layout:$constraintLayoutVersion"
}

object Support {
    private val supportLibraryVersion = "26.0.0-beta2"
    private val googlePlayServicesVersion = "11.8.0"

    val placesApi = "com.google.android.gms:play-services-places:$googlePlayServicesVersion"
    val design = "com.android.support:design:$supportLibraryVersion"
    val recyclerView = "com.android.support:recyclerview-v7:$supportLibraryVersion"
    val cardView = "com.android.support:cardview-v7:$supportLibraryVersion"
    val appCompat = "com.android.support:appcompat-v7:$supportLibraryVersion"
    val appCompatV4 = "com.android.support:support-v4:$supportLibraryVersion"
    val appCompatV13 = "com.android.support:support-v13:$supportLibraryVersion"
    val annotation = "com.android.support:support-annotations:$supportLibraryVersion"
    val dataBinding = "com.android.databinding:compiler:${PlatformDeps.androidPlugin}"
}


object Rx {
    private val javaVersion = "2.0.6"
    private val androidVersion = "2.0.1"
    private val permissionsVersion = "0.9.5@aar"
    private val kotlinVersion = "2.2.0"
    private val bindingVersion = "2.0.0"

    val java = "io.reactivex.rxjava2:rxjava:$javaVersion"
    val kotlin = "io.reactivex.rxjava2:rxkotlin:$kotlinVersion"
    val android = "io.reactivex.rxjava2:rxandroid:$androidVersion"
    val binding = "com.jakewharton.rxbinding2:rxbinding-kotlin:$bindingVersion"
    val permissions = "com.tbruyelle.rxpermissions2:rxpermissions:$permissionsVersion"
}

object Dagger {
    private val daggerVersion = "2.11"

    val compiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    val processor = "com.google.dagger:dagger-android-processor:$daggerVersion"
    val di = "com.google.dagger:dagger:$daggerVersion"
    val diSupport = "com.google.dagger:dagger-android-support:$daggerVersion"
}

object Glide {
    private val glideVersion = "4.1.1"
    private val glideTransformationsVersion = "3.0.1"

    val glide = "com.github.bumptech.glide:glide:$glideVersion"
    val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"
    val glideTransformations = "jp.wasabeef:glide-transformations:$glideTransformationsVersion"
}

object Squareup {
    private val retrofitVersion = "2.3.0"
    private val loggingInterceptorVersion = "3.8.1"
    private val leakCanaryVersion = "1.5.4"

    val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    val retrofitGson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
    val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$loggingInterceptorVersion"
    val leakCanaryDebug = "com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion"
    val leakCanaryRelease = "com.squareup.leakcanary:leakcanary-android-no-op:$leakCanaryVersion"
    val leakCanaryTest = "com.squareup.leakcanary:leakcanary-android-no-op:$leakCanaryVersion"
}

object PaperParcel {
    private val paperParcelVersion = "2.0.1"

    val library = "nz.bradcampbell:paperparcel:$paperParcelVersion"
    val kotlin = "nz.bradcampbell:paperparcel-kotlin:$paperParcelVersion"
    val compiler = "nz.bradcampbell:paperparcel-compiler:$paperParcelVersion"
}

object Tests {
    private val mockitoCoreVersion = "2.12.0"
    private val junitVersion = "4.12"
    private val espressoCoreVersion = "3.0.1"
    private val androidTestRunnerVersion = "1.0.1"
    private val dexmakerVersion = "1.2"

    val junit = "junit:junit:$junitVersion"
    val dexmaker = "com.google.dexmaker:dexmaker:$dexmakerVersion"
    val dexmakerMockito = "com.google.dexmaker:dexmaker-mockito:$dexmakerVersion"
    val mockitoCore = "org.mockito:mockito-core:$mockitoCoreVersion"
    val espressoCore = "com.android.support.test.espresso:espresso-core:$espressoCoreVersion"
    val testRunner = "com.android.support.test:runner:$androidTestRunnerVersion"
    val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${PlatformDeps.kotlinVersion}"
}
