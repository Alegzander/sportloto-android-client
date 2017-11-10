package com.vovasoft.unilot.ui.fragments

import android.arch.lifecycle.Observer
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.robinhood.ticker.TickerUtils
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.ui.dialogs.ParticipateDialog
import com.vovasoft.unilot.ui.dialogs.TopPlacesDialog
import kotlinx.android.synthetic.main.fragment_game_daily.*


/***************************************************************************
 * Created by arseniy on 16/09/2017.
 ****************************************************************************/
class GameDailyFragment : GameBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_game_daily, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }


    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(context).registerReceiver(messageReceiver, IntentFilter("daily"))
    }


    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(context).unregisterReceiver(messageReceiver)
    }


    private fun observeData() {
        showLoading(true)
        gamesVM.getDailyGame().observe(this, Observer { game ->
            showLoading(false)
            Log.e("observeData", game.toString())
            this.game = game
            setupViews()
        })
    }


    override fun setupViews() {
        contentFrame.visibility = View.INVISIBLE
        noContentFrame.visibility = View.VISIBLE

        game?.let { game ->
            contentFrame.visibility = View.VISIBLE
            noContentFrame.visibility = View.INVISIBLE

            prizeBoard.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
            prizeBoard.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
            prizeBoard.setText("%.3f".format(game.prizeAmount), true)

            prizeFiatTv.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
            prizeFiatTv.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
            prizeFiatTv.setText("$ %.2f".format(game.prizeAmountFiat), true)

            peopleTv.text = game.playersNum.toString()

            if (game.status == Game.Status.PUBLISHED.value) {
                showPublished()
            }
            else {
                showFinishing()
            }

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
    }


    private fun showFinishing() {
        publishedView.visibility = View.GONE
        finishingView.visibility = View.VISIBLE

        game?.let { game ->
            walletTv.text = game.smartContractId

            copyBtn.setOnClickListener {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("wallet", walletTv.text)
                clipboard.primaryClip = clip
                Toast.makeText(context, R.string.wallet_number_has_been_copied, Toast.LENGTH_SHORT).show()
            }

            countDown = object : CountDownTimer(game.endTime() - System.currentTimeMillis(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / (1000 * 60)) % 60
                    view?.let {
                        calculateTimeTv.text = String.format("%02d : %02d", minutes, seconds)
                    }
                }

                override fun onFinish() {

                }
            }
            countDown?.start()
        }
    }


    private fun showPublished() {
        publishedView.visibility = View.VISIBLE
        finishingView.visibility = View.GONE

        game?.let { game ->
            timeProgress?.setProgress(0)

            countDown = object : CountDownTimer(game.endTime() - System.currentTimeMillis(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / (1000 * 60)) % 60
                    val hours = (millisUntilFinished / (1000 * 60 * 60))
                    val progress = ((millisUntilFinished * 100) / (game.endTime() - game.startTime())).toInt()

                    view?.let {
                        timeTv.text = String.format("%02d : %02d : %02d", hours, minutes, seconds)
                        timeProgress?.setProgress(progress)
                    }
                }

                override fun onFinish() {

                }
            }
            countDown?.start()

            betTv.text = "%.2f Eth".format(game.betAmount)

            participateBtn.setOnClickListener {
                val dialog = ParticipateDialog(context, game)
                dialog.show()
            }
        }
    }

}
