package com.vovasoft.sportloto.ui.recycler_adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.repository.models.Winner
import com.vovasoft.sportloto.ui.view_holders.TopPlacesViewHolder

/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class TopPlacesRecyclerAdapter : RecyclerView.Adapter<TopPlacesViewHolder>() {

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


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopPlacesViewHolder {
        var view: View? = null

        when(viewType) {
            ViewType.WHITE.value -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_top_places_white, parent,false)
            }
            ViewType.GRAY.value -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_top_places_gray, parent,false)
            }
        }

        return TopPlacesViewHolder(view)
    }


    override fun onBindViewHolder(holder: TopPlacesViewHolder?, position: Int) {
        holder?.setData(dataSet[position])
    }

}