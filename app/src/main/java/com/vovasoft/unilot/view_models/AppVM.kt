package com.vovasoft.unilot.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/***************************************************************************
 * Created by arseniy on 01/10/2017.
 ****************************************************************************/
class AppVM : ViewModel() {
    var selectedPage = MutableLiveData<Int>()
    var languageChanged = MutableLiveData<Boolean>()

    init {
        selectedPage.value = 0
        languageChanged.value = false
    }
}