package com.swenggco.contactapp.injections.modules

import android.content.Context
import com.swenggco.contactapp.AndroidApp
import com.swenggco.contactapp.injections.AppContext


import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: AndroidApp) {

    @Provides
    @Singleton
    fun provideApp() = app

    @Provides
    @AppContext
    internal fun provideContext(): Context {
        return app.applicationContext
    }
}