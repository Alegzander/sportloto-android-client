package com.vovasoft.unilot.ui.recycler_adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.Winner
import com.vovasoft.unilot.ui.view_holders.WinnersViewHolder

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class WinnersRecyclerAdapter : RecyclerView.Adapter<WinnersViewHolder>() {

    enum class ViewType(val value: Int) {
        WHITE(0), GRAY(1);

        companion object {
            fun from(findValue: Int) : ViewType {
                return if (findValue % 2 == 0) {
                    WHITE
                } else {
                    GRAY
                }
            }
        }
    }


    var dataSet = mutableListOf<Winner>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }


    override fun getItemViewType(position: Int): Int {
        return ViewType.from(position).value
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WinnersViewHolder {
        var view: View? = null

        when(viewType) {
            ViewType.WHITE.value -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_winners_white, parent,false)
            }
            ViewType.GRAY.value -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_winners_gray, parent,false)
            }
        }

        return WinnersViewHolder(view)
    }


    override fun onBindViewHolder(holder: WinnersViewHolder?, position: Int) {
        holder?.setData(dataSet[position])
    }

}