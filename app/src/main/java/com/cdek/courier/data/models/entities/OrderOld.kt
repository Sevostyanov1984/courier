package com.cdek.android.kotlinapp.source.models.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cdek.courier.data.models.Packages
import com.cdek.courier.utils.CREATE_DATE_TEMPLATE
import com.cdek.courier.utils.NOT_HANDED
import java.text.SimpleDateFormat
import java.util.*

data class OrderOld(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var createDate: String? = SimpleDateFormat(CREATE_DATE_TEMPLATE, Locale.getDefault()).format(
        Date()
    ),
    var photoLocal: Int? = NOT_HANDED,
    var photoRemote: Int? = NOT_HANDED,
    var hasDD: Int? = NOT_HANDED,
    var deliverydate: String? = "",
    var user_id: String?,
    var number: String?,
    var deleted: String?,
    var dateTimeCreate: String?,
    var canPayByCard: String? = "false",
    var isPaidSertificate: String? = "false",
    var declaredCost: String?,
    var sender: String?,
    var receiver: String?,
//                    @TypeConverters(PackagesConverter::class)
    var packages: Packages?,
    var declaredCostCurrencyCode: String?,
    var note: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("packages"),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(createDate)
        parcel.writeValue(photoLocal)
        parcel.writeValue(photoRemote)
        parcel.writeValue(hasDD)
        parcel.writeString(deliverydate)
        parcel.writeString(user_id)
        parcel.writeString(number)
        parcel.writeString(deleted)
        parcel.writeString(dateTimeCreate)
        parcel.writeString(canPayByCard)
        parcel.writeString(isPaidSertificate)
        parcel.writeString(declaredCost)
        parcel.writeString(sender)
        parcel.writeString(receiver)
        parcel.writeString(declaredCostCurrencyCode)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderOld> {
        override fun createFromParcel(parcel: Parcel): OrderOld {
            return OrderOld(parcel)
        }

        override fun newArray(size: Int): Array<OrderOld?> {
            return arrayOfNulls(size)
        }
    }

}
