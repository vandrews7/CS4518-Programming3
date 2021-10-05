package com.example.basketballcounter.database

import androidx.room.Dao
import androidx.room.Query
import com.example.basketballcounter.Game
import java.util.*

@Dao
interface GameDao {

    @Query("SELECT * FROM game")
    fun getGames(): List<Game>

    @Query("SELECT * FROM game WHERE id=(:id)")
    fun getGame(id: UUID) Game?

    @Query("SELECT * FROM game WHERE scoreA > scoreB")
    fun getAWins(): List<Game>

    @Query("SELECT * FROM game WHERE scoreB > scoreA")
    fun getBWins(): List<Game>
}