package com.swenggco.contactapp.injections.components

import com.swenggco.contactapp.fragments.ContactDetailFragment
import com.swenggco.contactapp.fragments.ContactListFragment
import com.swenggco.contactapp.injections.ViewScope
import com.swenggco.contactapp.injections.modules.ViewModule
import dagger.Subcomponent


@ViewScope
@Subcomponent(modules = [(ViewModule::class)])
interface ViewComponent {

    fun inject(contactListFragment: ContactListFragment)

    fun inject(contactDetailFragment: ContactDetailFragment)
}
