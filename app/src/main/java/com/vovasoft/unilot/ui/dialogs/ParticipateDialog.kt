package com.vovasoft.unilot.ui.dialogs

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.text.Html
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Toast
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.repository.models.entities.Game
import kotlinx.android.synthetic.main.dialog_view_participate.view.*
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.AppCompatImageView
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager


/***************************************************************************
 * Created by arseniy on 12/10/2017.
 ****************************************************************************/
class ParticipateDialog(val context: Context, val game: Game) {

    private val participateDialogView: ParticipateDialogView
    private var dialog: AlertDialog? = null


    init {
        participateDialogView = ParticipateDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(participateDialogView)
        dialog = builder.create()
        dialog?.show()
    }


    fun dismiss() {
        dialog?.dismiss()
    }


    inner class ParticipateDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr) {

        private var countDown: CountDownTimer? = null

        init {
            inflate(context, R.layout.dialog_view_participate, this)
            setupViews()
        }


        private fun setupViews() {
            closeBtn.setOnClickListener {
                dismiss()
            }

            walletTv.text = game.smartContractId

            copyBtn.setOnClickListener {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("wallet", walletTv.text)
                clipboard.primaryClip = clip
                Toast.makeText(context, R.string.wallet_number_has_been_copied, Toast.LENGTH_SHORT).show()

                when (game.type) {
                    Game.Type.DAILY.value -> {
                        Answers.getInstance().logCustom(CustomEvent("EVENT_DAILY_PARTICIPATE_COPY")
                                .putCustomAttribute("language", Preferences.instance.language))
                    }
                    Game.Type.WEEKLY.value -> {
                        Answers.getInstance().logCustom(CustomEvent("EVENT_WEEKLY_PARTICIPATE_COPY")
                                .putCustomAttribute("language", Preferences.instance.language))
                    }
                }

            }

            qrBtn.setOnClickListener {
                val builder = Dialog(context)
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE)

                val imageView = AppCompatImageView(context)
                imageView.setImageResource(R.drawable.ic_qr_code_black)
                builder.addContentView(imageView, RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT))

                val lp: WindowManager.LayoutParams  = WindowManager.LayoutParams()
                lp.copyFrom(builder.window.attributes)
                lp.width = width
                lp.height = width
                builder.show()
                builder.window.attributes = lp
            }

            participateTv.text = Html.fromHtml(context.getString(R.string.participation_rules).format(game.betAmount, 210000, 30))

            when (game.type) {
                Game.Type.DAILY.value -> {
                    var timeLeft = "00:00:00"
                    countDown = object : CountDownTimer(game.endTime() - System.currentTimeMillis(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val seconds = (millisUntilFinished / 1000) % 60
                            val minutes = (millisUntilFinished / (1000 * 60)) % 60
                            val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24

                            timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                            timeTv.text = timeLeft
                        }

                        override fun onFinish() { }
                    }
                    timeTv.text = timeLeft
                    titleTv.setText(R.string.daily_lottery_unilot)
                    countDown?.start()

                }
                Game.Type.WEEKLY.value -> {
                    var timeLeft = "00:00:00"
                    countDown = object : CountDownTimer(game.endTime() - System.currentTimeMillis(), 60000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val minutes = (millisUntilFinished / (1000 * 60)) % 60
                            val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24
                            val days = (millisUntilFinished / (1000 * 60 * 60 * 24))

                            timeLeft = String.format("%02d:%02d:%02d", days, hours, minutes)
                            timeTv.text = timeLeft
                        }

                        override fun onFinish() { }
                    }
                    timeTv.text = timeLeft
                    titleTv.setText(R.string.weekly_lottery_unilot)
                    countDown?.start()

                }
            }
        }


        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            countDown?.cancel()
        }

    }

}