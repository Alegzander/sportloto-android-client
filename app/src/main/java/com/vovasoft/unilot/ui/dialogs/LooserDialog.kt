package com.vovasoft.unilot.ui.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.daysPlural
import com.vovasoft.unilot.components.toTextHumanDate
import com.vovasoft.unilot.repository.models.entities.Game
import kotlinx.android.synthetic.main.dialog_view_looser.view.*

/***************************************************************************
 * Created by arseniy on 22/10/2017.
 ****************************************************************************/
class LooserDialog(val context: Context, val game: Game) {

    private val looserDialogView: LooserDialogView
    private var dialog: AlertDialog? = null
    private var onHistoryListener: (()-> Unit)? = null
    private var onDismissListener: (()-> Unit)? = null

    init {
        looserDialogView = LooserDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(looserDialogView)
        dialog = builder.create()
        dialog?.setOnDismissListener({
            onDismissListener?.invoke()
        })
        dialog?.show()
    }


    fun setDays(time: Long) {
        looserDialogView.setDays(time)
    }


    fun setonHistoryListener(listener: (()-> Unit)) {
        onHistoryListener = listener
    }


    fun setOnDismissListener(listener: (()-> Unit)) {
        onDismissListener = listener
    }


    fun dismiss() {
        dialog?.dismiss()
    }



    inner class LooserDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr) {

        init {
            inflate(context, R.layout.dialog_view_looser, this)
            setupViews()
        }


        private fun setupViews() {
            closeBtn.setOnClickListener {
                dismiss()
            }

            (when (game.type) {
                Game.Type.DAILY.value -> resultTv.setText(R.string.results_of_daily_drawing)
                Game.Type.WEEKLY.value -> resultTv.setText(R.string.results_of_weekly_drawing)
                Game.Type.MONTHLY.value -> resultTv.setText(R.string.results_of_monthly_drawing)
            })

            dateTv.text = game.endTime().toTextHumanDate()

            historyBtn.setOnClickListener {
                onHistoryListener?.invoke()
            }
        }


        fun setDays(time: Long) {
            val days = (time - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)
            daysTv.text = "%02d".format(days)
            mutableDaysTv.text = daysPlural(context, days.toInt(), context.getString(R.string.mutable_days))
        }

    }

}