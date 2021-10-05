package com.example.basketballcounter

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import java.io.File

private const val TAG = "ScoreViewModel"

class ScoreViewModel : ViewModel(){
    init {
        Log.i(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "ViewModel instance is about to be destroyed")
    }

    private var scoreA = 0
    private var scoreB = 0
    private var winPressed = 0
    var savePressed = false

    fun resetScoreA(): String {
        scoreA = 0
        return scoreA.toString()
    }
    fun resetScoreB(): String {
        scoreB = 0
        return scoreB.toString()
    }
    fun getScoreA(): Int {
        return scoreA
    }
    fun getScoreB(): Int {
        return scoreB
    }
    fun getScore(): String {
        winPressed += 1
        when{
            scoreA - scoreB < 0 -> return "Team B wins!"
            scoreA - scoreB > 0 -> return "Team A wins!"
            scoreA - scoreB == 0 -> return "It's a tie!"
            else -> return ""
        }
    }
    fun getWinPressed(): Int {
        return winPressed
    }
    fun resetWinPressed() {
        winPressed = 0
    }
    fun addScoreA(value: Int): String{
        scoreA += value
        return scoreA.toString()
    }
    fun addScoreB(value: Int): String{
        scoreB += value
        return scoreB.toString()
    }

    fun getPhotoFile(game: Game): File {
        return GameRepository.get().getPhotoFile(game)
    }

}