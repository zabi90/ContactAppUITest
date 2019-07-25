package com.swenggco.contactapp.injections.modules

import androidx.fragment.app.FragmentActivity
import dagger.Module

@Module class ViewModule(val activity: FragmentActivity?){
    //put view level dependency here
}