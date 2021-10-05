package com.example.basketballcounter

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "table_game")
data class Game(@PrimaryKey val id: String = "",
                var teamAName: String = "",
                var teamBName: String = "",
                var teamAScore: Int = 0,
                var teamBScore: Int = 0,
                var date: Int = 0
){
    val photoFileName
        get() = "IMG_$id.jpg"
}