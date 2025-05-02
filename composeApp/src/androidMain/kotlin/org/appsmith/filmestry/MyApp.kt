package org.appsmith.filmestry

import android.app.Application
import org.appsmith.filmestry.shared.AppContext

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.setUp(applicationContext)
    }
}