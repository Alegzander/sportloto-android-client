package com.vovasoft.unilot.ui.fragments

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.R
import com.vovasoft.unilot.ui.AppFragmentManager
import com.vovasoft.unilot.ui.pager_adapters.TutorialPagerAdapter
import kotlinx.android.synthetic.main.fragment_tutorial.*

/***************************************************************************
 * Created by arseniy on 17/11/2017.
 ****************************************************************************/
class TutorialFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_tutorial, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lockDrawerMode(true)

        val pagerAdapter = TutorialPagerAdapter(childFragmentManager)
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager, true)

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == pagerAdapter.count - 1) {
                    skipBtn.visibility = View.INVISIBLE
                    endLayout.visibility = View.VISIBLE
                }
                else {
                    skipBtn.visibility = View.VISIBLE
                    endLayout.visibility = View.INVISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        skipBtn.setOnClickListener {
            onBackPressed()
        }

        letsgoBtn.setOnClickListener {
            onBackPressed()
        }

        knowMoreBtn.setOnClickListener {
            AppFragmentManager.instance.openFragment(FAQFragment(), true)
        }

    }


    override fun onBackPressed() {
        lockDrawerMode(false)
        AppFragmentManager.instance.popBackStack()
    }

}