package com.vovasoft.unilot.components

import java.text.SimpleDateFormat
import java.util.*

/***************************************************************************
 * Created by arseniy on 01/10/2017.
 ****************************************************************************/

fun Long?.toHumanDate() : String {
    val formatter = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    return if (this != null) formatter.format(this) else ""
}
