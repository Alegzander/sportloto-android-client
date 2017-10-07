package com.vovasoft.sportloto.repository

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
interface RepositoryCallback<in T> {
    fun done(data: T? = null)
}