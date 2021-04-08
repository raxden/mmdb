package com.raxdenstudios.app.network

class APIDataV4Provider : APIDataProvider {

  override fun getDomain(): String = "https://api.themoviedb.org/4/"

  override fun getImageDomain(): String = "https://image.tmdb.org/t/p/"

  override fun getToken(): String =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3ZTkyYzk2Y2JkYmMxMWIzZjUyYzZmZWZlNDk1MGI5NiIsInN1YiI6IjYwNGY3N2Q0MTdiNWVmMDA2YmViNTAyZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.K6xG2lv4Wmi-fLR3aUS4YR7nAWUxX7tm__2wsOCyz38"
}
