package com.awu0.notetakeoff

import android.app.Application
import com.awu0.notetakeoff.data.AppContainer
import com.awu0.notetakeoff.data.AppDataContainer

class NoteApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}