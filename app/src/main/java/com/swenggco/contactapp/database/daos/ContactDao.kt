package com.swenggco.contactapp.database.daos

import androidx.room.*
import com.swenggco.contactapp.models.Contact
import io.reactivex.Single

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact")
    fun getAll(): Single<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact): Single<Long>

    @Query("DELETE FROM contact WHERE contact.id =:id")
    fun deleteContact(id: Int): Single<Int>

    @Update
    fun updateContact(contact: Contact): Single<Int>
}