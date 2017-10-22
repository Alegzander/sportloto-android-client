package com.vovasoft.unilot.ui.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.Game
import kotlinx.android.synthetic.main.dialog_view_winner.view.*

/***************************************************************************
 * Created by arseniy on 22/10/2017.
 ****************************************************************************/
class WinnerDialog(val context: Context, val game: Game) {

    private val winnerDialogView: WinnerDialogView
    private var dialog: AlertDialog? = null


    init {
        winnerDialogView = WinnerDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(winnerDialogView)
        dialog = builder.create()
        dialog?.show()
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


        private fun setupViews() {
            closeBtn.setOnClickListener {
                dismiss()
            }
        }

    }

}