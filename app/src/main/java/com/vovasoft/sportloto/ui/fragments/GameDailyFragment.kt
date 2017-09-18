package com.vovasoft.sportloto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.sportloto.R
import kotlinx.android.synthetic.main.fragment_game_daily.*

/***************************************************************************
 * Created by arseniy on 16/09/2017.
 ****************************************************************************/
class GameDailyFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_game_daily, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        tabDigit.setChar(2)
    }
}
