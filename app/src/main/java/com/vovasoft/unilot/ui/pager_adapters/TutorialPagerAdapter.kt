package com.vovasoft.unilot.ui.pager_adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.vovasoft.unilot.ui.fragments.TutorialPageFragment

/***************************************************************************
 * Created by arseniy on 13/11/2017.
 ****************************************************************************/
class TutorialPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            TutorialPageFragment.newInstance(1),
            TutorialPageFragment.newInstance(2),
            TutorialPageFragment.newInstance(3),
            TutorialPageFragment.newInstance(4),
            TutorialPageFragment.newInstance(5),
            TutorialPageFragment.newInstance(6),
            TutorialPageFragment.newInstance(7)
    )


    override fun getCount(): Int {
        return fragments.size
    }


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}
