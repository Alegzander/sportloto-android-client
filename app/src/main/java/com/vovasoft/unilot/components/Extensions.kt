package com.vovasoft.unilot.components

import android.content.Context
import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/***************************************************************************
 * Created by arseniy on 01/10/2017.
 ****************************************************************************/

fun Long?.toHumanDate() : String {
    val formatter = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    return if (this != null) formatter.format(this) else ""
}

fun Long?.toTextHumanDate() : String {
    val df = DateFormat.getDateInstance(DateFormat.FULL)
    return if (this != null) df.format(this) else ""
}

fun daysPlural(context: Context, num: Int, word: String) : String {
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        context.resources.configuration.locales.get(0)
    } else{
        context.resources.configuration.locale
    }

    if (locale.language != "ru") {
        return word
    }

    val variant = num % 100

    return when (variant) {
        in 2..4 -> "Дня"
        in 5..20 -> "Дней"
        in 22..24 -> "Дня"
        in 25..30 -> "Дней"
        in 32..34 -> "Дня"
        in 35..40 -> "Дней"
        else -> "День"
    }
}