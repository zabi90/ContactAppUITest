package com.swenggco.contactapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.swenggco.contactapp.database.daos.ContactDao
import com.swenggco.contactapp.models.Contact



@Database(entities = arrayOf(Contact::class), version = 1)
abstract class ContactDataBase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}