package com.vovasoft.unilot.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.R
import kotlinx.android.synthetic.main.fragment_tutorial_page.*

/***************************************************************************
 * Created by arseniy on 13/11/2017.
 ****************************************************************************/
class TutorialPageFragment : Fragment() {

    companion object {
        fun newInstance(page: Int): TutorialPageFragment {
            val fragment = TutorialPageFragment()
            val args = Bundle()
            args.putInt("page", page)
            fragment.arguments = args
            return fragment
        }
    }


    private val page: Int
        get() = arguments?.getInt("page") ?: 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tutorial_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }


    private fun setupView() {
        when(page) {
            1 -> {
                tutorialTv.setText(R.string.tutorial_step_1)
                tutorialImg.setImageResource(R.drawable.tutorial_1)
            }
            2 -> {
                tutorialTv.setText(R.string.tutorial_step_2)
                tutorialImg.setImageResource(R.drawable.tutorial_2)
            }
            3 -> {
                tutorialTv.setText(R.string.tutorial_step_3)
                tutorialImg.setImageResource(R.drawable.tutorial_3)
            }
            4 -> {
                tutorialTv.setText(R.string.tutorial_step_4)
                tutorialImg.setImageResource(R.drawable.tutorial_4)
            }
            5 -> {
                tutorialTv.setText(R.string.tutorial_step_5)
                tutorialImg.setImageResource(R.drawable.tutorial_5)
            }
        }
    }
}
