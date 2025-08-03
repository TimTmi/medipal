package com.example.medipal

import android.app.Application
import com.example.medipal.di.AppContainer
import com.example.medipal.di.DefaultAppContainer

class MediPalApplication : Application() {
    
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
