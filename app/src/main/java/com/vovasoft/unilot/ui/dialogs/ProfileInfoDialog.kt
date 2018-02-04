package com.vovasoft.unilot.ui.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import kotlinx.android.synthetic.main.dialog_view_profile_info.view.*

/***************************************************************************
 * Created by arseniy on 08/11/2017.
 ****************************************************************************/
class ProfileInfoDialog(val context: Context) {

    private val profileInfoDialogView: ProfileInfoDialogView
    private var dialog: AlertDialog? = null

    init {
        profileInfoDialogView = ProfileInfoDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(profileInfoDialogView)
        dialog = builder.create()
        dialog?.show()
    }


    fun dismiss() {
        dialog?.dismiss()
    }



    inner class ProfileInfoDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr) {

        init {
            inflate(context, R.layout.dialog_view_profile_info, this)
            setupViews()
        }


        private fun setupViews() {
            okBtn.setOnClickListener {
                dismiss()
            }
        }

    }

}