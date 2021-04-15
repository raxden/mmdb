# https://docs.gradle.org/current/release-notes.html

GRADLE_WRAPPER_VERSION="7.0"

function updateGradleWrapperVersion {
  cd ..
  ./gradlew wrapper --gradle-version=$GRADLE_WRAPPER_VERSION
}

updateGradleWrapperVersion
