package com.vovasoft.unilot.ui.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult
import kotlinx.android.synthetic.main.dialog_view_looser.view.*

/***************************************************************************
 * Created by arseniy on 22/10/2017.
 ****************************************************************************/
class LooserDialog(val context: Context, val game: Game, val result: GameResult) {

    private val looserDialogView: LooserDialogView
    private var dialog: AlertDialog? = null


    init {
        looserDialogView = LooserDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(looserDialogView)
        dialog = builder.create()
        dialog?.setOnDismissListener({
            result.deleteAsync()
        })
        dialog?.show()
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
        }

    }

}