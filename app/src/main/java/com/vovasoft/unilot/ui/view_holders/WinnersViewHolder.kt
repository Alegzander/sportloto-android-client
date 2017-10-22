package com.vovasoft.unilot.ui.view_holders

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.Winner

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class WinnersViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val placeTv = itemView?.findViewById<AppCompatTextView>(R.id.placeTv)
    private val prizeTv = itemView?.findViewById<AppCompatTextView>(R.id.prizeTv)
    private val prizeFiatTv = itemView?.findViewById<AppCompatTextView>(R.id.prizeFiatTv)

    fun setData(data: Winner) {
        placeTv?.text = data.position?.toString()
        prizeTv?.text = "%.4f".format(data.prize)
        prizeFiatTv?.text = "%.2f".format(data.prizeFiat)
    }
}