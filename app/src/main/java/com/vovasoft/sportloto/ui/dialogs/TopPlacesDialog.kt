package com.vovasoft.sportloto.ui.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.repository.models.TopPlace
import com.vovasoft.sportloto.ui.recycler_adapters.TopPlacesRecyclerAdapter
import kotlinx.android.synthetic.main.dialog_view_top_places.view.*

/***************************************************************************
 * Created by arseniy on 11/10/2017.
 ****************************************************************************/
class TopPlacesDialog(val context: Context) {

    private val topPlacesDialogView: TopPlacesDialogView
    private var dialog: AlertDialog? = null


    init {
        topPlacesDialogView = TopPlacesDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(topPlacesDialogView)
        dialog = builder.create()
        dialog?.show()
    }


    fun dismiss() {
        dialog?.dismiss()
    }



    inner class TopPlacesDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr) {

        init {
            inflate(context, R.layout.dialog_view_top_places, this)
            setupViews()
        }


        private fun setupViews() {
            closeBtn.setOnClickListener {
                dismiss()
            }

            val adapter = TopPlacesRecyclerAdapter()
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

            val dataSet = (1..13).map { TopPlace(place = it.toString(), prize = it * 12f, prizeFiat = it * 21f) }

            adapter.dataSet = dataSet.toMutableList()
        }

    }

}