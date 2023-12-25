plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.android.chatsapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.android.chatsapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

    buildTypes {

        debug {
            isDebuggable=true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    //for binding view
    viewBinding{
        enable =true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        dataBinding =true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-basement:18.2.0")
    implementation("androidx.browser:browser:1.2.0")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.fragment:fragment:1.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //otp view
    implementation("com.github.mukeshsolanki.android-otpview-pinview:otpview:3.1.0")
//    firebase
    implementation ("com.google.firebase:firebase-bom:32.4.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx:23.0.3")
    implementation ("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation ("com.google.firebase:firebase-analytics-ktx:21.5.0")

    //Kotlin's coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    //circle imageview for profile picture
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //to load image in layout from url
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //OkHttp
    implementation ("com.squareup.okhttp3:okhttp:4.9.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.2")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    //picasso for image downloading and caching library for Android
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("androidx.fragment:fragment-ktx:1.3.6")

    //android reaction
    implementation("com.github.pgreze:android-reactions:1.6")

    //pictures
    implementation("io.ak1.pix:piximagepicker:1.6.3")

    //observable
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //data binding
    implementation("androidx.databinding:databinding-runtime:7.0.0")
    implementation("androidx.databinding:databinding-compiler:7.0.0")

    //FCM
    implementation("com.google.firebase:firebase-messaging:23.4.0")


    //status
    implementation("com.github.3llomi:CircularStatusView:V1.0.3")
    implementation("io.perfmark:perfmark-api:0.25.0")

    implementation("com.google.firebase:firebase-inappmessaging-display:20.4.0")

    //add lottie dependency
    implementation("com.airbnb.android:lottie:6.2.0")
    //Image picker
    implementation("com.github.dhaval2404:imagepicker:2.1")
}
kapt {
    correctErrorTypes = true
    generateStubs = true
}
