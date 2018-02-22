package com.vovasoft.unilot.ui.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.toTextHumanDate
import com.vovasoft.unilot.repository.models.entities.Game
import kotlinx.android.synthetic.main.dialog_view_unknown_status.view.*

/***************************************************************************
 * Created by arseniy on 08/11/2017.
 ****************************************************************************/
class UnknownStatusDialog(val context: Context, val game: Game) {

    private val unknownStatusDialogView: UnknownStatusDialogView
    private var dialog: AlertDialog? = null
    private var onHistoryListener: (()-> Unit)? = null
    private var onDismissListener: (()-> Unit)? = null

    init {
        unknownStatusDialogView = UnknownStatusDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(unknownStatusDialogView)
        dialog = builder.create()
        dialog?.setOnDismissListener({
            onDismissListener?.invoke()
        })
        dialog?.show()
    }


    fun setOnHistoryListener(listener: (()-> Unit)) {
        onHistoryListener = listener
    }


    fun setOnDismissListener(listener: (()-> Unit)) {
        onDismissListener = listener
    }


    fun dismiss() {
        dialog?.dismiss()
    }



    inner class UnknownStatusDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr) {

        init {
            inflate(context, R.layout.dialog_view_unknown_status, this)
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

    }

}