package com.hamv.videogamesdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hamv.videogamesdb.data.db.model.GameEntity
import com.hamv.videogamesdb.util.Constants

@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = true //Por si se quiere el esquema para una migracion
)
abstract class GameDatabase: RoomDatabase(){ //tiene que ser abstracta
    //Aqui va el DAO
    abstract fun gameDao(): GameDao

    //Esto para manejar Singleton

    companion object{

        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }


    }
}