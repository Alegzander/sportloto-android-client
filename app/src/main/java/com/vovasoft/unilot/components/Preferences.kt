package com.vovasoft.unilot.components

import android.content.Context
import android.content.SharedPreferences
import com.vovasoft.unilot.App
import java.util.*

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class Preferences private constructor() {

    private object Holder { val INSTANCE = Preferences() }

    companion object {
        val instance: Preferences by lazy { Holder.INSTANCE }

        var isLanguageChanged: Boolean = false


        fun updateLanguage() {
            val locale = Locale(Preferences.instance.language)
            Locale.setDefault(locale)
            val configuration = App.instance.resources.configuration
            configuration.locale = locale
            App.instance.resources.updateConfiguration(configuration, App.instance.resources.displayMetrics)

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                App.instance.createConfigurationContext(configuration)
//            }
//            else {
//                App.instance.resources.updateConfiguration(configuration, App.instance.resources.displayMetrics)
//            }
        }

    }

    private val sharedPreferences: SharedPreferences = App.instance.getSharedPreferences(Preferences::class.java.name, Context.MODE_PRIVATE)


    var isFirstOpen: Boolean
        set(value) = sharedPreferences.edit().putBoolean("is_first_open", value).apply()
        get() = sharedPreferences.getBoolean("is_first_open", true)


    var token: String?
        set(value) = sharedPreferences.edit().putString("token", value).apply()
        get() = sharedPreferences.getString("token", null)


    var dayLotteryNotify: Boolean
        set(value) = sharedPreferences.edit().putBoolean("day_lottery_notify", value).apply()
        get() = sharedPreferences.getBoolean("day_lottery_notify", true)


    var weekLotteryNotify: Boolean
        set(value) = sharedPreferences.edit().putBoolean("week_lottery_notify", value).apply()
        get() = sharedPreferences.getBoolean("week_lottery_notify", true)


    var monthLotteryNotify: Boolean
        set(value) = sharedPreferences.edit().putBoolean("month_lottery_notify", value).apply()
        get() = sharedPreferences.getBoolean("month_lottery_notify", true)


    var language: String
        set(value) = sharedPreferences.edit().putString("language", value).apply()
        get() = sharedPreferences.getString("language", Locale.getDefault().language)

}