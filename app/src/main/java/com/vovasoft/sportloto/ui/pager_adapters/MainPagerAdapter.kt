package com.vovasoft.sportloto.ui.pager_adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.vovasoft.sportloto.ui.fragments.*

/***************************************************************************
 * Created by arseniy on 17/09/2017.
 ****************************************************************************/
class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(GameDailyFragment(), GamesWeeklyFragment(), GameMonthlyFragment(), ProfileFragment())


    override fun getCount(): Int {
        return fragments.size
    }


    override fun getItem(position: Int): BaseFragment {
        return fragments[position]
    }

}
