package com.android.chatsapp.model

import android.os.Parcel
import android.os.Parcelable

data class UserStatus(
    val uid: String?,
    val name: String?,
    val profileImg: String?,
    val lastupdated:Long,
    val status: Array<Status>?

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.createTypedArray(Status)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(name)
        parcel.writeString(profileImg)
        parcel.writeLong(lastupdated)
        parcel.writeTypedArray(status, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserStatus> {
        override fun createFromParcel(parcel: Parcel): UserStatus {
            return UserStatus(parcel)
        }

        override fun newArray(size: Int): Array<UserStatus?> {
            return arrayOfNulls(size)
        }
    }

}