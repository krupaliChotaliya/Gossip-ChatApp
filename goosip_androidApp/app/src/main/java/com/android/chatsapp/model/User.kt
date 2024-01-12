package com.android.chatsapp.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val uid:String,
    val name: String,
    val about: String,
    val profileImg: String,
    var active:String,
    var status:String,
    var lastMessage:String ="No message",
    var fcmToken:String=""

) : Parcelable {

    constructor() : this("", "","","","","","","")

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(name)
        parcel.writeString(about)
        parcel.writeString(profileImg)
        parcel.writeString(active)
        parcel.writeString(status)
        parcel.writeString(lastMessage)
        parcel.writeString(fcmToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}