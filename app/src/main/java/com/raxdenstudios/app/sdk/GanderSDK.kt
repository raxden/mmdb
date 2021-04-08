package com.raxdenstudios.app.sdk

import com.ashokvarma.gander.Gander
import com.ashokvarma.gander.imdb.GanderIMDB

object GanderSDK {

  fun init() {
    Gander.setGanderStorage(GanderIMDB.getInstance())
  }
}
