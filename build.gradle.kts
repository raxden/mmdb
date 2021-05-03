plugins {
  id("com.vanniktech.android.junit.jacoco")
  id("com.raxdenstudios.android-releasing")
}

releasing {

}

junitJacoco {
  excludes = listOf(
    "**/di/*",
    "**/BuildConfig*",
    "**/databinding/*",
    "**/*_*.class",
    "**/*_Impl*.class"
  )
}

allprojects {
  repositories {
    google()
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
  }
}

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
