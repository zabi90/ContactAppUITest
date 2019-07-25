package com.swenggco.contactapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Contact(
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "last_name") var lastName: String,
    var title: String?,
    var telephone: String?,
    var email: String?,
    @Embedded var address: Address?,
    var note: String?,
    var organization: String?,
    @ColumnInfo(name = "date_of_birth") var dateBirth: String?
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

@Parcelize
data class Address(
    var street: String?,
    var country: String?,
    var city: String?,
    @ColumnInfo(name = "zip_code") var zipCode: String?
) : Parcelable

