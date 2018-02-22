package com.vovasoft.unilot.ui.view_holders

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.pure.Player

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class PlayersViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val addressTV = itemView?.findViewById<AppCompatTextView>(R.id.addressTV)

    fun setData(data: Player) {
        addressTV?.text = data.wallet
    }
}