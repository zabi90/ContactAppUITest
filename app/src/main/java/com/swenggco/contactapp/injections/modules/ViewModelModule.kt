package com.swenggco.contactapp.injections.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swenggco.contactapp.injections.ViewModelFactory
import com.swenggco.contactapp.injections.ViewModelKey
import com.swenggco.contactapp.viewmodels.ContactViewModel

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    internal abstract fun contactViewModel(viewModel: ContactViewModel): ViewModel
    //Add more ViewModels here

}