package com.example.basketballcounter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "MainActivity"
private const val REQUEST_CODE = 0

class MainActivity : AppCompatActivity(), GameListFragment.Callbacks  {

    private val scoreViewModel: ScoreViewModel by lazy {
        ViewModelProviders.of(this).get(ScoreViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "called onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null){
            val fragment = GameListFragment.newInstance() //this adds the recyclerViewer to the main activity
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onGameSelected(gameID: UUID) {
        //   Log.d(TAG, "MainActivity.onGameSelected: $id")
        val fragment = GameFragment.newInstance(gameID)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "called onDestroy()")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("scoreA", scoreViewModel.getScoreA())
        savedInstanceState.putInt("scoreB", scoreViewModel.getScoreB())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getInt("scoreA")
        savedInstanceState.getInt("scoreB")
    }
}