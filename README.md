Mobile movie database
==========

<a href='https://github.com/raxden/mmdb/actions/workflows/ci.yml'><img src='https://github.com/raxden/mmdb/workflows/Continuous%20Integration/badge.svg'></a>
[![codecov](https://codecov.io/gh/raxden/mmdb/branch/master/graph/badge.svg?token=UQoTMhwKPO)](https://codecov.io/gh/raxden/mmdb)

Mobile movie database is a simple app to search movies and tv shows. It uses [The Movie Database API](https://developers.themoviedb.org/3/getting-started/introduction) to get the data. Currently in development.

## How to build on your environment

Add your [The Movie DB](https://www.themoviedb.org)'s API key in your `./config/secrets.properties` file.
```xml
TMDB_TOKEN_V3=YOUR_API_KEY
TMDB_TOKEN_V4=YOUR_API_KEY
```

## Roadmap

- [x] Home
- [x] Medias by category
- [x] Dark mode
- [x] Media detail (WIP)
- [ ] Player
- [ ] Search
- [ ] Account

## Guidelines

- https://github.com/android/nowinandroid
- https://developer.android.com/topic/architecture/ui-layer 
- https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates 
- https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95

## Goals

The goal of this project is to do practice on the new technologies, patterns and styles. Some functionalities are still not implemented or not completed.

- [x] MVVM
- [x] Kotlin
- [x] Room
  - [x] Migration
  - [ ] Type converters
- [x] Coroutines
  - [x] Flow
  - [x] StateFlow
  - [ ] SharedFlow
- [x] Hilt
- [x] Compose
  - [x] Navigation
    - [x] NavHost
  - [x] LaunchedEffect
  - [x] DisposableEffect
- [x] Junit
  - [x] Mockk
  - [x] Turbine
  - [x] Google Truth
  - [ ] Robolectric
  - [ ] Parameterized tests
- [x] Instrumentation tests
  - [x] Espresso
  - [x] Using Hilt to inject dependencies into tests
  - [x] Create largeTest using RestMock and Room in memory.
  - [x] MockWebServer
- [x] CI
  - [x] Github actions
  - [x] Composite actions
  - [x] Coverage
    - [x] Codecov integration
    - [ ] Sonarqube integration
  - [x] Detekt integration
  - [ ] Snapshot testing
  - [x] Firebase app distribution
  - [ ] Google Play integration
- [x] Architecture
  - [x] Modularization by features
  - [ ] Dynamic features
  - [ ] Kotlin Multiplatform
- [ ] Create catalog app module
- [x] Dependencies
  - [x] Use gradle kotlin dsl
  - [x] Use gradle plugin portal
  - [x] Version catalog (.toml)
  - [ ] Use composite build - https://stackoverflow.com/questions/60464719/gradle-includebuild-vs-implementation-project
