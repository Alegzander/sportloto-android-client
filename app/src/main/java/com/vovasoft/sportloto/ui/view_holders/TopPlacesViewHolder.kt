package com.vovasoft.sportloto.ui.view_holders

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.repository.models.TopPlace

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class TopPlacesViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val placeTv = itemView?.findViewById<AppCompatTextView>(R.id.placeTv)
    private val prizeTv = itemView?.findViewById<AppCompatTextView>(R.id.prizeTv)
    private val prizeFiatTv = itemView?.findViewById<AppCompatTextView>(R.id.prizeFiatTv)

    fun setData(data: TopPlace) {
        placeTv?.text = data.place
        prizeTv?.text = data.prize.toString()
        prizeFiatTv?.text = data.prizeFiat.toString()
    }
}