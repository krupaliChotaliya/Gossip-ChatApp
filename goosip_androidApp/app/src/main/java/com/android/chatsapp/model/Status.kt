package com.android.chatsapp.model

import android.os.Parcel
import android.os.Parcelable

data class Status(
    val imageurl: String?,
    val timestamp:Long
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageurl)
        parcel.writeLong(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Status> {
        override fun createFromParcel(parcel: Parcel): Status {
            return Status(parcel)
        }

        override fun newArray(size: Int): Array<Status?> {
            return arrayOfNulls(size)
        }
    }
}