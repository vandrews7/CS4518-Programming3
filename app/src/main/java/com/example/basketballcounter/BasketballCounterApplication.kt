package com.example.basketballcounter

import android.app.Application

class BasketballCounterApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GameRepository.initialize(this)
    }
}