package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.R
import com.vovasoft.unilot.ui.pager_adapters.MainPagerAdapter
import com.vovasoft.unilot.view_models.AppVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_pager.*


/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class MainPagerFragment : BaseFragment() {

    enum class Page(val value: Int) {
        DAY(0), WEEK(1), MONTH(2), PROFILE(3);

        companion object {
            fun from(findValue: Int) : Page {
                return when (findValue) {
                    0 -> DAY
                    1 -> WEEK
                    2 -> MONTH
                    else -> PROFILE
                }
            }
        }
    }

    private lateinit var appVM: AppVM


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main_pager, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appVM = ViewModelProviders.of(activity).get(AppVM::class.java)
        drawerBtn.setOnClickListener { activity.drawerLayout.openDrawer(GravityCompat.START) }
        setupPager()
        observeData()
    }

    private fun observeData() {
        appVM.selectedPage.observe(this, Observer {
            for (i in 0..tabs.tabCount) {
                val tab = tabs.getTabAt(i)
                tab?.icon?.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGray), PorterDuff.Mode.SRC_IN)
                if (i == it) {
                    tab?.icon?.setColorFilter(ContextCompat.getColor(context, R.color.colorOrange), PorterDuff.Mode.SRC_IN)
                }
            }
        })
    }


    private fun setupPager() {

        infoBtn.setOnClickListener {
            AlertDialog.Builder(context)
                    .setTitle(R.string.how_does_it_work)
                    .setMessage(R.string.how_does_it_work_text)
                    .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .create().show()
        }

        val pagerAdapter = MainPagerAdapter(childFragmentManager)
        pager.adapter = pagerAdapter

        val position = appVM.selectedPage.value!!

        tabs.setupWithViewPager(pager)
        tabs.getTabAt(Page.DAY.value)?.setText(R.string.daily)?.setIcon(R.drawable.ic_day_gray)
        tabs.getTabAt(Page.WEEK.value)?.setText(R.string.weekly)?.setIcon(R.drawable.ic_week_gray)
        tabs.getTabAt(Page.MONTH.value)?.setText(R.string.monthly_bonus)?.setIcon(R.drawable.ic_month_gray)
        tabs.getTabAt(Page.PROFILE.value)?.setText(R.string.profile)?.setIcon(R.drawable.ic_profile_gray)

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val x = ((pager.width * position + positionOffsetPixels) * computeFactor()).toInt()
                scrollView.scrollTo(x, 0)
            }

            override fun onPageSelected(position: Int) {
                appVM.selectedPage.value = position
                activity.hideKeyboard()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            private fun computeFactor(): Float {
                return (backgroundImg.width - pager.width) / (pager.width * (pager.adapter.count - 1)).toFloat()
            }
        })

        Handler().post {
            view?.let {
                pager.setCurrentItem(position, false)
            }
        }
    }

}
