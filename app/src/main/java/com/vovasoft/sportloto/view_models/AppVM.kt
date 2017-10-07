package com.vovasoft.sportloto.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/***************************************************************************
 * Created by arseniy on 01/10/2017.
 ****************************************************************************/
class AppVM : ViewModel() {
    var selectedPage = MutableLiveData<Int>()

    init {
        selectedPage.value = 0
    }
}