package com.vovasoft.sportloto.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.repository.models.Game
import com.vovasoft.sportloto.view_models.GamesVM
import kotlinx.android.synthetic.main.fragment_game_monthly.*

/***************************************************************************
 * Created by arseniy on 16/09/2017.
 ****************************************************************************/
class GameMonthlyFragment : BaseFragment() {

    private val gamesVM: GamesVM
        get() = ViewModelProviders.of(activity).get(GamesVM::class.java)


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_game_monthly, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }


    private fun observeData() {
        showLoading(true)
        gamesVM.getMonthlyGame().observe(this, Observer { game ->
            showLoading(false)
            if (game == null) {
                contentFrame.visibility = View.INVISIBLE
                noContentFrame.visibility = View.VISIBLE
            }
            else {
                Log.e("observeData", game.toString())
                setupViews(game)
                contentFrame.visibility = View.VISIBLE
                noContentFrame.visibility = View.INVISIBLE
            }
        })
    }


    private fun setupViews(game: Game) {
        prizeCryptoTv.text = String.format("%.2f", game.prizeAmount)
        prizeFiatTv.text = String.format("$ %.2f", game.prizeAmountFiat)
        peopleTv.text = game.playersNum.toString()

        val days = (game.endTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)
        daysValueTv.text = days.toString()
    }


    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }
}
