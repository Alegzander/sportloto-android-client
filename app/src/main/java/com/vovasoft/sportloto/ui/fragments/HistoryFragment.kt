package com.vovasoft.sportloto.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.repository.models.Game
import com.vovasoft.sportloto.ui.dialogs.TopPlacesDialog
import com.vovasoft.sportloto.view_models.GamesVM

/***************************************************************************
 * Created by arseniy on 15/10/2017.
 ****************************************************************************/
class HistoryFragment : BaseFragment() {

    private val gamesVM: GamesVM
        get() = ViewModelProviders.of(activity).get(GamesVM::class.java)


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }


    private fun observeData() {
        showLoading(true)
        gamesVM.getMonthlyGame().observe(this, Observer { game ->
            showLoading(false)

        })
    }


    private fun setupViews(game: Game) {
        val dialog = TopPlacesDialog(context, game)
        dialog.show()
    }


    override fun showLoading(show: Boolean) {
//        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }
}
