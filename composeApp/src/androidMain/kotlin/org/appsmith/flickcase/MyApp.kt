package org.appsmith.flickcase

import android.app.Application
import org.appsmith.flickcase.shared.AppContext

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.setUp(applicationContext)
    }
}