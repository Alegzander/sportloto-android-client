package com.vovasoft.sportloto.ui.fragments

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.sportloto.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_pager.*

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class MainPagerFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main_pager, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawerBtn.setOnClickListener { activity.drawerLayout.openDrawer(GravityCompat.START) }
    }
}
