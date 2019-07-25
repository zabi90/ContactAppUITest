package com.swenggco.contactapp.injections.modules


import android.content.Context
import androidx.room.Room
import com.swenggco.contactapp.database.ContactDataBase
import com.swenggco.contactapp.database.DataBaseHelper
import com.swenggco.contactapp.injections.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun getDataBaseHelper(@AppContext context: Context): DataBaseHelper {
        return DataBaseHelper(
            dataBase = Room.databaseBuilder(
                context,
                ContactDataBase::class.java, DataBaseHelper.DATA_BASE_NAME
            ).build()
        )
    }

}