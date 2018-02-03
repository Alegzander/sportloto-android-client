package com.vovasoft.unilot.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/***************************************************************************
 * Created by arseniy on 01/10/2017.
 ****************************************************************************/
class AppSettings : ViewModel() {
    var gamePageChanged = MutableLiveData<Boolean>()

    init {
        gamePageChanged.value = false
    }

    companion object {
        var selectedPage: Int = 0
        var selectedHistoryFilter: Int = 0
    }
}