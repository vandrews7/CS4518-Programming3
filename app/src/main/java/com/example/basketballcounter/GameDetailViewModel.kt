package com.example.basketballcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class GameDetailViewModel(): ViewModel() {
    private val gameRepository = GameRepository.get()
    private val gameIdLiveData = MutableLiveData<String>()

    var gameLiveData: LiveData<Game?> =
        Transformations.switchMap(gameIdLiveData) { gameId ->
            gameRepository.getGame(gameId)
        }

    fun loadGame(gameId: String) {
        gameIdLiveData.value = gameId
    }

    fun saveGame(game: Game) {
        gameRepository.updateCrime(game)
    }

    fun getPhotoFile(game: Game): File {
        return gameRepository.getPhotoFile(game)
    }
}