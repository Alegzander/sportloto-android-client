package com.vovasoft.unilot.ui.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.pure.Winner
import com.vovasoft.unilot.ui.recycler_adapters.WinnersRecyclerAdapter
import kotlinx.android.synthetic.main.dialog_view_top_places.view.*

/***************************************************************************
 * Created by arseniy on 11/10/2017.
 ****************************************************************************/
class TopPlacesDialog(val context: Context, val game: Game) {

    private val topPlacesDialogView: TopPlacesDialogView
    private var dialog: AlertDialog? = null


    init {
        topPlacesDialogView = TopPlacesDialogView(context)
    }


    fun setWinners(winners: List<Winner>) {
        topPlacesDialogView.setWinnersData(winners)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(topPlacesDialogView)
        dialog = builder.create()
        dialog?.show()
    }


    fun dismiss() {
        dialog?.dismiss()
    }



    inner class TopPlacesDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr) {

        private val adapter = WinnersRecyclerAdapter(game)

        init {
            inflate(context, R.layout.dialog_view_top_places, this)
            setupViews()
        }


        private fun setupViews() {
            closeBtn.setOnClickListener {
                dismiss()
            }

            progressBar.visibility = View.VISIBLE

            val prizeText = if (game.type == Game.Type.TOKEN.value) {
                "${"%.0f".format(game.prize?.amount)} ${game.prize?.currency} = ${"$ %.2f".format(game.prizeAmountFiat)}"
            }
            else {
                "${"%.4f".format(game.prize?.amount)} ${game.prize?.currency} = ${"$ %.2f".format(game.prizeAmountFiat)}"
            }
            prizeTv.text = prizeText
            currencyTv.text = game.prize?.currency
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }


        fun setWinnersData(winners: List<Winner>) {
            adapter.dataSet = winners.toMutableList()
            progressBar.visibility = View.INVISIBLE
        }

    }

}