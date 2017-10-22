package com.vovasoft.unilot.ui.view_holders

import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.Wallet

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class WalletsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val walletTv = itemView?.findViewById<AppCompatTextView>(R.id.walletTv)
    private val deleteBtn = itemView?.findViewById<AppCompatImageButton>(R.id.deleteBtn)

    lateinit var wallet: Wallet

    fun setData(data: Wallet) {
        wallet = data
        walletTv?.text = wallet.number
    }

    fun setOnDeleteListener(listener: (Wallet) -> Unit) {
        deleteBtn?.setOnClickListener {
            listener(wallet)
        }
    }
}