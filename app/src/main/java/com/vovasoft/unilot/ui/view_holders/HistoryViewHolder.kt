package com.vovasoft.unilot.ui.view_holders

import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.toHumanDate
import com.vovasoft.unilot.repository.models.entities.Game

/***************************************************************************
 * Created by arseniy on 21/10/2017.
 ****************************************************************************/
class HistoryViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val iconImg = itemView?.findViewById<AppCompatImageView>(R.id.iconImg)
    private val dateTv = itemView?.findViewById<AppCompatTextView>(R.id.dateTv)
    private val statusTv = itemView?.findViewById<AppCompatTextView>(R.id.statusTv)
    private val prizeTv = itemView?.findViewById<AppCompatTextView>(R.id.prizeTv)

    private var onItemClick: (() -> Unit)? = null

    init {
        itemView?.setOnClickListener(this)
    }

    fun setData(game: Game) {
        when (game.type) {
            Game.Type.DAILY.value -> iconImg?.setImageResource(R.drawable.ic_day_gray)
            Game.Type.WEEKLY.value -> iconImg?.setImageResource(R.drawable.ic_week_gray)
            Game.Type.MONTHLY.value -> iconImg?.setImageResource(R.drawable.ic_month_gray)
        }

        dateTv?.text = game.endTime().toHumanDate()

        when (game.status) {
            Game.Status.PUBLISHED.value -> {
                statusTv?.text = "В процессе"
                prizeTv?.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorBlue))
                prizeTv?.text = "перейти"
            }
            Game.Status.FINISHED.value -> {
                statusTv?.text = "Завершен"
                prizeTv?.text = "нет" //TODO if {winner} then {prize} else no
            }
        }
    }


    fun setOnItemClickListener(listener: () -> Unit) {
        onItemClick = listener
    }


    override fun onClick(v: View?) {
        onItemClick?.invoke()
    }
}