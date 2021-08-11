package com.teamxdevelopers.teamx.database.saved

import android.os.Parcel
import android.os.Parcelable


data class SavedParcelable(
    val title:String,
    val thumbnail:String,
    val postId:String,
    val published:String,
    val content:String,
    val id:Int,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readInt()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(title)
            writeString(thumbnail)
            writeString(postId)
            writeString(published)
            writeString(content)
            writeInt(id)
        }
    }

    companion object CREATOR : Parcelable.Creator<SavedParcelable> {
        override fun createFromParcel(parcel: Parcel): SavedParcelable {
            return SavedParcelable(parcel)
        }

        override fun newArray(size: Int): Array<SavedParcelable?> {
            return arrayOfNulls(size)
        }
    }
}