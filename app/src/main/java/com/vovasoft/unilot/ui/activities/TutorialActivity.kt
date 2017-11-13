package com.vovasoft.unilot.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.vovasoft.unilot.R
import com.vovasoft.unilot.ui.pager_adapters.TutorialPagerAdapter
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val pagerAdapter = TutorialPagerAdapter(supportFragmentManager)
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager, true)
    }
}
