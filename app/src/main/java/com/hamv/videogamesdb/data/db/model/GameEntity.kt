package com.hamv.videogamesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hamv.videogamesdb.util.Constants

@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    val id: Long = 0,

    @ColumnInfo(name = "game_title")
    var title: String,

    @ColumnInfo(name = "game_genere")
    var genere:String,

    @ColumnInfo(name = "game_developer", defaultValue = "Desconocido")
    var developer: String
)
