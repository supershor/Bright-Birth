import org.gradle.internal.impldep.org.bouncycastle.util.Properties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.om_tat_sat.brightbirth"
    compileSdk = 34
    bundle{
        language{
            enableSplit = false
        }
    }
    defaultConfig {
        applicationId = "com.om_tat_sat.brightbirth"
        minSdk = 28
        targetSdk = 34
        versionCode = 9
        versionName = "1.3.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildTypes {
        release {
            isMinifyEnabled = false
            android.buildFeatures.buildConfig=true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    ("25.1.8937393")
    externalNativeBuild {
        cmake {
            path ("CMakeLists.txt")
        }
    }
}

dependencies {

    //noinspection UseTomlInstead
    implementation("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")


    //noinspection UseTomlInstead
    implementation("com.google.ai.client.generativeai:generativeai:0.5.0")

    // Required for one-shot operations (to use `ListenableFuture` from Guava Android)
    //noinspection UseTomlInstead
    implementation("com.google.guava:guava:32.0.1-android")

    // Required for streaming operations (to use `Publisher` from Reactive Streams)
    //noinspection UseTomlInstead
    implementation("org.reactivestreams:reactive-streams:1.0.4")

    //noinspection UseTomlInstead
    implementation ("com.airbnb.android:lottie:6.2.0")
    //noinspection UseTomlInstead
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    //noinspection UseTomlInstead
    implementation ("com.intuit.ssp:ssp-android:1.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}