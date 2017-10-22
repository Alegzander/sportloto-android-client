package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.robinhood.ticker.TickerUtils
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.Game
import com.vovasoft.unilot.ui.dialogs.TopPlacesDialog
import com.vovasoft.unilot.view_models.GamesVM
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
        prizeBoard.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
        prizeBoard.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        prizeBoard.setText("%.4f".format(game.prizeAmount), true)

        prizeFiatTv.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
        prizeFiatTv.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        prizeFiatTv.setText("$ %.2f".format(game.prizeAmountFiat), true)

        peopleTv.text = game.playersNum.toString()

        val days = (game.endTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)
        daysBoard.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
        daysBoard.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        daysBoard.setText("%02d".format(days), true)

        topPlacesBtn.setOnClickListener {
            val dialog = TopPlacesDialog(context, game)
            game.id?.let {
                gamesVM.getWinners(it).observe(this, Observer { winners ->
                    dialog.setWinners(winners ?: emptyList())
                })
            }
            dialog.show()
        }

    }


    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }
}
