package com.jn.echomaze

import android.app.Application
import com.jn.echomaze.di.AppContainer
import com.jn.echomaze.di.AppDataContainer

class MainApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
