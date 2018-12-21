package davydov.annamoney.application

import android.app.Application
import davydov.annamoney.di.AppComponent
import davydov.annamoney.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}