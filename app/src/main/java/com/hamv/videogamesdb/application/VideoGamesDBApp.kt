package com.hamv.videogamesdb.application

import android.app.Application
import com.hamv.videogamesdb.data.GameRepository
import com.hamv.videogamesdb.data.db.GameDatabase

class VideoGamesDBApp: Application() {
    private val database by lazy{
        GameDatabase.getDatabase(this@VideoGamesDBApp)
    }

    val repository by lazy {
        GameRepository(database.gameDao())
    }

}