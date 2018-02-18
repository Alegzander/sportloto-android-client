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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.robinhood.ticker.TickerUtils
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.components.daysPlural
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.ui.dialogs.BonusParticipateDialog
import com.vovasoft.unilot.ui.dialogs.TopPlacesDialog
import com.vovasoft.unilot.ui.widgets.ZxingReader
import kotlinx.android.synthetic.main.fragment_game_monthly.*

/***************************************************************************
 * Created by arseniy on 16/09/2017.
 ****************************************************************************/
class GameMonthlyFragment : GameBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_monthly, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Answers.getInstance().logCustom(CustomEvent("EVENT_BONUS")
                .putCustomAttribute("language", Preferences.instance.language))
    }


    override fun onStart() {
        super.onStart()
        context?.let { context ->
            LocalBroadcastManager.getInstance(context).registerReceiver(messageReceiver, IntentFilter("monthly"))
        }
    }


    override fun onStop() {
        super.onStop()
        context?.let { context ->
            LocalBroadcastManager.getInstance(context).unregisterReceiver(messageReceiver)
        }
    }


    private fun observeData() {
        showLoading(true)
        gamesVM.getMonthlyGame().observe(this, Observer { game ->
            showLoading(false)
            this.game = game
            setupViews()
        })
    }


    override fun setupViews() {
        context?.let { context ->
            contentFrame.visibility = View.INVISIBLE
            noContentFrame.visibility = View.VISIBLE

            game?.let { game ->
                contentFrame.visibility = View.VISIBLE
                noContentFrame.visibility = View.INVISIBLE

                prizeBoard.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
                prizeBoard.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                prizeBoard.setText("%.4f".format(game.prize?.amount), true)

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
    }


    private fun showFinishing() {
        publishedView.visibility = View.GONE
        finishingView.visibility = View.VISIBLE

        game?.let { game ->
            walletTv.text = game.smartContractId

            copyBtn.setOnClickListener {
                val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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
        context?.let { context ->
            publishedView.visibility = View.VISIBLE
            finishingView.visibility = View.GONE

            game?.let { game ->

                val days = (game.endTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24) + 1
                daysBoard.setCharacterList(TickerUtils.getDefaultListForUSCurrency())
                daysBoard.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                daysBoard.setText("%02d".format(days), true)
                mutableDaysTv.text = daysPlural(context, days.toInt(), getString(R.string.mutable_days))

                infoBtn.setOnClickListener {
                    val dialog = BonusParticipateDialog(context, game)
                    game.id?.let {
                        gamesVM.getPlayers(it).observe(this, Observer { winners ->
                            dialog.setPlayers(winners ?: emptyList())
                        })

                        gamesVM.getWallets().observe(this, Observer { wallets ->
                            dialog.setWallets(wallets ?: emptyList())
                        })
                    }
                    onScannerResult = dialog.onScannerResult()
                    dialog.setOnQrCodePressed {
                        if (hasCameraPermission) {
                            runQReader()
                        } else {
                            requestPermissions(listOf(ZxingReader.CAMERA_PERMISSION).toTypedArray(), ZxingReader.CAMERA_PERMISSION_CODE)
                        }
                    }
                    dialog.show()
                    Answers.getInstance().logCustom(CustomEvent("EVENT_LIST_PARTICIPANTS")
                            .putCustomAttribute("language", Preferences.instance.language))
                }
            }
        }
    }

}
