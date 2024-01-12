package com.android.chatsapp.model

import android.os.Parcel
import android.os.Parcelable

data class UserStory(
    var uid: String?,
    var name: String?,
    var profileImg: String?,
    var stories: ArrayList<Story>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Story.CREATOR) ?: ArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(name)
        parcel.writeString(profileImg)
        parcel.writeTypedList(stories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserStory> {
        override fun createFromParcel(parcel: Parcel): UserStory {
            return UserStory(parcel)
        }

        override fun newArray(size: Int): Array<UserStory?> {
            return arrayOfNulls(size)
        }
    }
}
