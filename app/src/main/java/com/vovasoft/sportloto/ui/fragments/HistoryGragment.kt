package com.vovasoft.sportloto.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.robinhood.ticker.TickerUtils
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.repository.models.Game
import com.vovasoft.sportloto.ui.dialogs.TopPlacesDialog
import com.vovasoft.sportloto.view_models.GamesVM
import kotlinx.android.synthetic.main.fragment_game_monthly.*

/***************************************************************************
 * Created by arseniy on 15/10/2017.
 ****************************************************************************/
class HistoryGragment : BaseFragment() {

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
        prizeBoard.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
        prizeBoard.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        prizeBoard.setText("%.2f".format(game.prizeAmount), true)

        prizeFiatTv.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
        prizeFiatTv.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        prizeFiatTv.setText("$ %.2f".format(game.prizeAmountFiat), true)

        peopleTv.text = game.playersNum.toString()

        val days = (game.endTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)
        daysBoard.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
        daysBoard.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        daysBoard.setText("22", true)

//        daysBoard.setValue(days.toString())

        topPlacesBtn.setOnClickListener {
            val dialog = TopPlacesDialog(context)
            dialog.show()
        }

    }


    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }
}
