package com.vovasoft.sportloto

import android.content.Context
import android.content.SharedPreferences

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class Preferences private constructor() {

    private object Holder { val INSTANCE = Preferences() }

    companion object {
        val instance: Preferences by lazy { Holder.INSTANCE }
    }

    private val sharedPreferences: SharedPreferences = App.instance!!.getSharedPreferences(Preferences::class.java.name, Context.MODE_PRIVATE)

    var token: String?
        set(value) = sharedPreferences.edit().putString("token", value).apply()
        get() = sharedPreferences.getString("token", null)

}