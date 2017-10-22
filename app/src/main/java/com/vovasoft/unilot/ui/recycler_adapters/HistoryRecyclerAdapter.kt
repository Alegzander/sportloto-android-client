package com.vovasoft.unilot.ui.recycler_adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.Game
import com.vovasoft.unilot.ui.view_holders.HistoryViewHolder

/***************************************************************************
 * Created by arseniy on 21/10/2017.
 ****************************************************************************/
class HistoryRecyclerAdapter : RecyclerView.Adapter<HistoryViewHolder>() {

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


    private var onItemClick: ((Game) -> Unit)? = null


    var dataSet = mutableListOf<Game>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }


    fun setOnItemClickListener(listener: (Game) -> Unit) {
        onItemClick = listener
    }


    override fun getItemViewType(position: Int): Int {
        return ViewType.from(position).value
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryViewHolder {
        var view: View? = null

        when(viewType) {
            ViewType.WHITE.value -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_history_white, parent,false)
            }
            ViewType.GRAY.value -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_history_gray, parent,false)
            }
        }

        return HistoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: HistoryViewHolder?, position: Int) {
        holder?.setData(dataSet[position])
        holder?.setOnItemClickListener { onItemClick?.invoke(dataSet[position]) }
    }

}