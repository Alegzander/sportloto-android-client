package com.vovasoft.unilot.ui.pager_adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.Fragment
import com.vovasoft.unilot.ui.fragments.TutorialPageFragment

/***************************************************************************
 * Created by arseniy on 13/11/2017.
 ****************************************************************************/
class TutorialPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            TutorialPageFragment(),
            TutorialPageFragment(),
            TutorialPageFragment(),
            TutorialPageFragment(),
            TutorialPageFragment()
    )


    override fun getCount(): Int {
        return fragments.size
    }


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}
