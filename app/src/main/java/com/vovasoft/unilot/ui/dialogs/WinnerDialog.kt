package com.vovasoft.unilot.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.entities.GameResult
import kotlinx.android.synthetic.main.dialog_view_winner.view.*

/***************************************************************************
 * Created by arseniy on 22/10/2017.
 ****************************************************************************/
class WinnerDialog(val context: Context, val result: GameResult) {

    private val winnerDialogView: WinnerDialogView
    private var dialog: AlertDialog? = null
    private var onHistoryListener: (()-> Unit)? = null
    private var onDismissListener: (()-> Unit)? = null

    init {
        winnerDialogView = WinnerDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(winnerDialogView)
        dialog = builder.create()
        dialog?.setOnDismissListener({
            onDismissListener?.invoke()
        })
        dialog?.show()
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



    inner class WinnerDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr) {

        init {
            inflate(context, R.layout.dialog_view_winner, this)
            setupViews()
        }


        @SuppressLint("SetTextI18n")
        private fun setupViews() {
            closeBtn.setOnClickListener {
                dismiss()
            }

            placeTv.text = context.getString(R.string.you_take_place_d).format(result.position)
            prizeTv.text = "%.4f".format(result.prize?.amount)
            prizeFialtTv.text = "US $%.2f".format(result.prizeFiat)

            historyBtn.setOnClickListener {
                onHistoryListener?.invoke()
            }
        }

    }

}