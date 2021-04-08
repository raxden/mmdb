plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryBase))

  api(Libraries.glide)
  api(Libraries.glideCompiler)
}
