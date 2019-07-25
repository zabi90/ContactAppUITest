package com.swenggco.contactapp.injections.components

import com.swenggco.contactapp.AndroidApp
import com.swenggco.contactapp.injections.modules.AppModule
import com.swenggco.contactapp.injections.modules.DataModule
import com.swenggco.contactapp.injections.modules.ViewModelModule
import com.swenggco.contactapp.injections.modules.ViewModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, ViewModelModule::class])

interface AppComponent {

    fun inject(app: AndroidApp)

    fun plus(viewModule: ViewModule): ViewComponent

}