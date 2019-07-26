package com.swenggco.contactapp.injections.components.modules


import android.content.Context
import androidx.room.Room
import com.swenggco.contactapp.database.ContactDataBase
import com.swenggco.contactapp.database.DataBaseHelper
import com.swenggco.contactapp.injections.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestDataModule {

    @Provides
    fun getDataBaseHelper(@AppContext context: Context): DataBaseHelper {
        return DataBaseHelper(
            Room.inMemoryDatabaseBuilder(
                context, ContactDataBase::class.java
            )
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build()
        )
    }

}