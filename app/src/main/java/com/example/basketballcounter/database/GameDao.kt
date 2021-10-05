package com.example.basketballcounter.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.basketballcounter.Game
import java.util.*

@Dao
interface GameDao {

    @Query("SELECT * FROM game")
    fun getGames(): LiveData<List<Game>>

    @Query("SELECT * FROM game WHERE id=(:id)")
    fun getGame(id: String): LiveData<Game?>

    @Query("SELECT * FROM game WHERE teamAScore > teamBScore")
    fun getAWins(): LiveData<List<Game>>

    @Query("SELECT * FROM game WHERE teamBScore > teamAScore")
    fun getBWins(): LiveData<List<Game>>
}