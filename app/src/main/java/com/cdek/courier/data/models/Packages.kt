package com.cdek.courier.data.models

import android.os.Parcel
import android.os.Parcelable

data class Packages(
    var id: String?,
    var description: String?,
    var length: String?,
    var width: String?,
    var height: String?,
    var realWeight: String?,
    var volumeWeight: String?,
    var packageNum: String?,
    var barcode: String?,
    var ec4Id: String?,
    var calcWeight: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeString(length)
        parcel.writeString(width)
        parcel.writeString(height)
        parcel.writeString(realWeight)
        parcel.writeString(volumeWeight)
        parcel.writeString(packageNum)
        parcel.writeString(barcode)
        parcel.writeString(ec4Id)
        parcel.writeString(calcWeight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Packages> {
        override fun createFromParcel(parcel: Parcel): Packages {
            return Packages(parcel)
        }

        override fun newArray(size: Int): Array<Packages?> {
            return arrayOfNulls(size)
        }
    }
}