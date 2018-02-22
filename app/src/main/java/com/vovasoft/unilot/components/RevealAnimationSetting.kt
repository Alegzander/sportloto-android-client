package com.vovasoft.unilot.components

import android.os.Parcel
import android.os.Parcelable

/***************************************************************************
 * Created by arseniy on 03/02/2018.
 ****************************************************************************/
class RevealAnimationSetting(
        var centerX: Int = 0,
        var centerY: Int = 0,
        var width: Double = 0.0,
        var height: Double = 0.0) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(centerX)
        parcel.writeInt(centerY)
        parcel.writeDouble(width)
        parcel.writeDouble(height)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RevealAnimationSetting> {
        override fun createFromParcel(parcel: Parcel): RevealAnimationSetting {
            return RevealAnimationSetting(parcel)
        }

        override fun newArray(size: Int): Array<RevealAnimationSetting?> {
            return arrayOfNulls(size)
        }
    }
}