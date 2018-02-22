package com.vovasoft.unilot.ui.view_holders

import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.toHumanDate
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.GameResult

/***************************************************************************
 * Created by arseniy on 21/10/2017.
 ****************************************************************************/
class HistoryViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val iconImg = itemView?.findViewById<AppCompatImageView>(R.id.iconImg)
    private val dateTv = itemView?.findViewById<AppCompatTextView>(R.id.dateTv)
    private val statusTv = itemView?.findViewById<AppCompatTextView>(R.id.statusTv)
    private val prizeTv = itemView?.findViewById<AppCompatTextView>(R.id.prizeTv)
    private val markerView = itemView?.findViewById<View>(R.id.markerView)

    private var onItemClick: (() -> Unit)? = null

    init {
        itemView?.setOnClickListener(this)
    }

    fun setData(game: Game, results: List<GameResult>) {
        when (game.type) {
            Game.Type.DAILY.value -> iconImg?.setImageResource(R.drawable.ic_day_gray)
            Game.Type.WEEKLY.value -> iconImg?.setImageResource(R.drawable.ic_week_gray)
            Game.Type.MONTHLY.value -> iconImg?.setImageResource(R.drawable.ic_month_gray)
            Game.Type.TOKEN.value -> iconImg?.setImageResource(R.drawable.ic_token_gray)
        }

        if (results.any { it.gameId == game.id }) {
            markerView?.visibility = View.VISIBLE
        }
        else {
            markerView?.visibility = View.INVISIBLE
        }

        dateTv?.text = game.endTime().toHumanDate()

        when (game.status) {
            Game.Status.PUBLISHED.value -> {
                statusTv?.text = itemView.context.getString(R.string.in_progress)
                prizeTv?.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                prizeTv?.text = itemView.context.getString(R.string.move)
            }
            Game.Status.FINISHED.value -> {
                statusTv?.text = itemView.context.getString(R.string.finished)
                prizeTv?.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorBlue))
                prizeTv?.text = itemView.context.getString(R.string.details)
            }
            Game.Status.CANCELLED.value -> {
                statusTv?.text = itemView.context.getString(R.string.canceled)
                prizeTv?.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorBlue))
                prizeTv?.text = itemView.context.getString(R.string.details)
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