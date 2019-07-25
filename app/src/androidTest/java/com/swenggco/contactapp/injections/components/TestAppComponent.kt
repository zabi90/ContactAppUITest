package com.swenggco.contactapp.injections.components

import com.swenggco.contactapp.injections.components.modules.TestDataModule
import com.swenggco.contactapp.injections.modules.AppModule
import com.swenggco.contactapp.injections.modules.DataModule
import com.swenggco.contactapp.injections.modules.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, TestDataModule::class, ViewModelModule::class])
interface TestAppComponent :AppComponent