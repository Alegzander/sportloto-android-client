package com.vovasoft.unilot.repository.models

import com.google.gson.Gson
import java.io.Serializable

/***************************************************************************
 * Created by arseniy on 28/10/2017.
 ****************************************************************************/
abstract class GsonModel : Serializable {

    companion object {

        private val gson = Gson()

        fun <T> fromJson(json: String, out: Class<T>): T {
            return gson.fromJson(json, out)
        }
    }


    fun toJson(): String {
        return gson.toJson(this)
    }

}
