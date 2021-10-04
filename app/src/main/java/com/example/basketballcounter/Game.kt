package com.example.basketballcounter

import java.util.*

data class Game(val id: UUID = UUID.randomUUID(),
                var index: String = "",
                var teamAname: String = "",
                var teamBname: String = "",
                var scoreA: Int = 0,
                var scoreB: Int = 0,
                var date: Date = Date()
)