package com.example.basketballcounter

import android.content.Context
import java.io.File
import androidx.room.Room
import com.example.basketballcounter.database.GameDatabase
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "game-database"

class GameRepository private constructor(context: Context){

    private val database : GameDatabase = Room.databaseBuilder(
        context.applicationContext,
        GameDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val gameDao = database.gameDao()

    fun getGames(): List<Game> = gameDao.getGames()

    fun getGame(id: UUID): Game? = gameDao.getGame(id)

    fun getAWins(): List<Game> = gameDao.getAWins()
//
    fun getBWins(): List<Game> = gameDao.getBWins()

    private val filesDir = context.applicationContext.filesDir

    fun getPhotoFile(game: Game): File = File(filesDir, game.photoFileName)

    companion object {
        private var INSTANCE: GameRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = GameRepository(context)
            }
        }

        fun get(): GameRepository {
            return INSTANCE ?:
            throw IllegalStateException("GameRepository must be initialized")
        }
    }

}