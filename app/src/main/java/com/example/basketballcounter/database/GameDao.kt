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
    fun getGame(id: UUID): LiveData<Game?>

    @Query("SELECT * FROM game WHERE scoreA > scoreB")
    fun getAWins(): LiveData<List<Game>>

    @Query("SELECT * FROM game WHERE scoreB > scoreA")
    fun getBWins(): LiveData<List<Game>>
}