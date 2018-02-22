package com.vovasoft.unilot.ui.view_holders

import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.Wallet
import com.vovasoft.unilot.repository.models.pure.Participate

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class WalletsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val walletTv = itemView?.findViewById<AppCompatTextView>(R.id.walletTv)
    private val dailyImg = itemView?.findViewById<AppCompatImageView>(R.id.dailyImg)
    private val weeklyImg = itemView?.findViewById<AppCompatImageView>(R.id.weeklyImg)
    private val monthlyImg = itemView?.findViewById<AppCompatImageView>(R.id.monthlyImg)
    private val tokenImg = itemView?.findViewById<AppCompatImageView>(R.id.tokenImg)
    private val deleteBtn = itemView?.findViewById<AppCompatImageButton>(R.id.deleteBtn)

    lateinit var wallet: Wallet

    fun setData(data: Wallet, participates: List<Participate>) {
        wallet = data
        walletTv?.text = wallet.number

        val types = participates.map { it.wallet to it.types }.toMap()[wallet.number]

        types?.forEach {
            when (Game.Type.from(it)) {
                Game.Type.DAILY -> dailyImg?.visibility = View.VISIBLE
                Game.Type.WEEKLY -> weeklyImg?.visibility = View.VISIBLE
                Game.Type.MONTHLY -> monthlyImg?.visibility = View.VISIBLE
                Game.Type.TOKEN -> tokenImg?.visibility = View.VISIBLE
            }
        }

    }

    fun setOnDeleteListener(listener: (Wallet) -> Unit) {
        deleteBtn?.setOnClickListener {
            listener(wallet)
        }
    }
}