package com.vovasoft.unilot.repository

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
interface Reactive<in T> {
    fun done(data: T? = null)
}   