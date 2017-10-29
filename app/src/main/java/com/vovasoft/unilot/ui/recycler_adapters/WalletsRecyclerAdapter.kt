package com.vovasoft.unilot.ui.recycler_adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.entities.Wallet
import com.vovasoft.unilot.ui.view_holders.WalletsViewHolder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class WalletsRecyclerAdapter : RecyclerView.Adapter<WalletsViewHolder>() {


    var dataSet = mutableListOf<Wallet>()

    private var onDelete: ((Wallet) -> Unit)? = null


    fun addWallets(wallets: List<Wallet>) {
        dataSet.clear()
        dataSet.addAll(wallets)
        notifyDataSetChanged()
    }


    fun addWallet(wallet: Wallet) {
        dataSet.add(wallet)
        notifyDataSetChanged()
    }


    fun deleteWallet(wallet: Wallet) {
        doAsync {
            dataSet.remove(wallet)
            wallet.delete()
            uiThread {
                notifyDataSetChanged()
            }
        }
    }


    fun setOnDeleteListener(listener: (Wallet) -> Unit) {
        onDelete = listener
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WalletsViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_wallets, parent,false)
        return WalletsViewHolder(view)
    }


    override fun onBindViewHolder(holder: WalletsViewHolder?, position: Int) {
        holder?.setData(dataSet[position])
        holder?.setOnDeleteListener {
            onDelete?.invoke(it)
        }
    }

}