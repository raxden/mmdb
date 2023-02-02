Mobile movie database
==========

<a href='https://github.com/raxden/mmdb/actions/workflows/ci.yml'><img src='https://github.com/raxden/mmdb/workflows/Continuous%20Integration/badge.svg'></a>
[![codecov](https://codecov.io/gh/raxden/mmdb/branch/master/graph/badge.svg?token=UQoTMhwKPO)](https://codecov.io/gh/raxden/mmdb)

Mobile movie database is a simple app to search movies and tv shows. It uses [The Movie Database API](https://developers.themoviedb.org/3/getting-started/introduction) to get the data. Currently in development.

## What is it?

The purpose of this app is to learn new technologies and improve my skills with android.

## Requirements

- [x] Show medias by modules
- [x] Show medias by category
- [ ] Search medias by query (WIP)
- [x] Show media detail (WIP)
- [ ] Show media trailer (WIP)

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
- [x] Coroutines
  - [x] Flow
  - [x] StateFlow
  - [ ] SharedFlow
- [x] Hilt
- [x] Compose
- [x] Junit
  - [x] Mockk
  - [x] Turbine
  - [x] Google Truth
- [x] Instrumentation tests
  - [x] Espresso
  - [x] Using Hilt to inject dependencies into tests
  - [ ] Create largeTest using RestMock and Room in memory.
- [x] CI
  - [x] Github actions
  - [x] Codecov integration
  - [x] Detekt integration
  - [x] Firebase app distribution
  - [x] Google Play integration
- [x] Modularization by features
- [ ] Dynamic feature modules with compose
- [ ] Create catalog app module
- [x] Version catalog for dependencies
- [ ] Parameterized tests
- [ ] Use composite build - https://stackoverflow.com/questions/60464719/gradle-includebuild-vs-implementation-project
