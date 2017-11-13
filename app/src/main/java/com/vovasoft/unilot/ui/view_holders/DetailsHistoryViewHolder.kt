package com.vovasoft.unilot.ui.view_holders

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.pure.Winner

/***************************************************************************
 * Created by arseniy on 22/10/2017.
 ****************************************************************************/
class DetailsHistoryViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {

    private val placeTv = itemView?.findViewById<AppCompatTextView>(R.id.placeTv)
    private val walletTv = itemView?.findViewById<AppCompatTextView>(R.id.walletTv)
    private val prizeTv = itemView?.findViewById<AppCompatTextView>(R.id.prizeTv)
    private val prizeFiatTv = itemView?.findViewById<AppCompatTextView>(R.id.prizeFiatTv)

    private var walletNumber: String? = null

    init {
        itemView?.setOnClickListener(this)
    }


    fun setData(data: Winner) {
        walletNumber = data.wallet
        placeTv?.text = data.position?.toString()
        walletTv?.text = data.wallet
        prizeTv?.text = "%.3f".format(data.prize)
        prizeFiatTv?.text = "%.2f".format(data.prizeFiat)
    }


    override fun onClick(v: View?) {
        val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("wallet", walletNumber)
        clipboard.primaryClip = clip
        Toast.makeText(itemView.context, R.string.wallet_number_has_been_copied, Toast.LENGTH_SHORT).show()
    }

}