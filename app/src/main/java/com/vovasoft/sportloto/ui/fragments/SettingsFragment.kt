package com.vovasoft.sportloto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.ui.AppFragmentManager
import kotlinx.android.synthetic.main.fragment_settings.*

/***************************************************************************
 * Created by arseniy on 01/10/2017.
 ****************************************************************************/
class SettingsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }


    private fun setupView() {
        backBtn.setOnClickListener {
            onBackPressed()
        }

        enSwitch.setOnClickListener {
            enSwitch.isChecked = true
            ruSwitch.isChecked = false
            deSwitch.isChecked = false
        }

        ruSwitch.setOnClickListener {
            enSwitch.isChecked = false
            ruSwitch.isChecked = true
            deSwitch.isChecked = false
        }

        deSwitch.setOnClickListener {
            enSwitch.isChecked = false
            ruSwitch.isChecked = false
            deSwitch.isChecked = true
        }
    }


    override fun onBackPressed() {
        AppFragmentManager.instance.popBackStack()
    }
}
