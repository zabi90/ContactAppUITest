package com.swenggco.contactapp

import android.app.Application
import com.swenggco.contactapp.injections.components.AppComponent
import com.swenggco.contactapp.injections.components.DaggerAppComponent
import com.swenggco.contactapp.injections.modules.AppModule


open class AndroidApp : Application() {

   open val component: AppComponent by lazy {

        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }


}