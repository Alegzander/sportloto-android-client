package com.vovasoft.unilot.ui.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import com.vovasoft.unilot.repository.models.entities.Game
import com.vovasoft.unilot.repository.models.entities.Wallet
import com.vovasoft.unilot.repository.models.pure.Player
import com.vovasoft.unilot.ui.recycler_adapters.PlayersRecyclerAdapter
import kotlinx.android.synthetic.main.dialog_view_participate_bonus.view.*
import java.util.regex.Pattern

/***************************************************************************
 * Created by arseniy on 11/10/2017.
 ****************************************************************************/
class BonusParticipateDialog(val context: Context, val game: Game) {

    private val participateDialogView: ParticipateDialogView
    private var dialog: AlertDialog? = null
    private var onQrCodePressed: (() -> Unit)? = null

    init {
        participateDialogView = ParticipateDialogView(context)
    }


    fun setPlayers(players: List<Player>) {
        participateDialogView.setPlayersData(players)
    }


    fun setWallets(wallets: List<Wallet>) {
        participateDialogView.setWalletsData(wallets)
    }


    fun setOnQrCodePressed(callback: (() -> Unit)) {
        onQrCodePressed = callback
    }


    fun onScannerResult(): (String?) -> Unit {
        return participateDialogView.onScannerResult
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(participateDialogView)
        dialog = builder.create()
        dialog?.show()
    }


    fun dismiss() {
        dialog?.dismiss()
    }



    inner class ParticipateDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr), SearchView.OnQueryTextListener {

        private val adapter = PlayersRecyclerAdapter()

        private var searchValue: String = ""

        private val players = mutableListOf<Player>()

        private val wallets = mutableListOf<Wallet>()

        var onScannerResult: ((String?) -> Unit) = { result ->
            searchBox?.setQuery(result, false)
        }


        init {
            inflate(context, R.layout.dialog_view_participate_bonus, this)
            setupViews()
        }


        private fun setupViews() {
            searchBox.setOnQueryTextListener(this)

            closeBtn.setOnClickListener {
                dismiss()
            }

            progressBar.visibility = View.VISIBLE

            when (game.type) {
                Game.Type.MONTHLY.value -> {
                    imageView.setImageResource(R.drawable.ic_ethereum_orange)
                    titleTv.setText(R.string.bonus_draw)
                    rulesTv.setText(R.string.participation_rules_bonus)
                }
                Game.Type.TOKEN.value -> {
                    imageView.setImageResource(R.drawable.ic_token_orange)
                    titleTv.setText(R.string.unit_draw)
                    rulesTv.setText(R.string.participation_rules_unit)
                }
            }


            scanBtn.setOnClickListener {
                onQrCodePressed?.invoke()
            }


            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }


        fun setPlayersData(players: List<Player>) {
            this.players.clear()
            this.players.addAll(players)
            refreshSearch()
            progressBar.visibility = View.INVISIBLE
        }


        fun setWalletsData(wallets: List<Wallet>) {
            adapter.wallets = wallets.toMutableList()
        }


        private fun refreshSearch() {
            adapter.dataSet = players.filter { winner ->
                winner.wallet?.toLowerCase()?.contains(Regex(Pattern.quote(searchValue.toLowerCase()))) == true
            }.toMutableList()
        }


        override fun onQueryTextSubmit(text: String?): Boolean {
            return true
        }


        override fun onQueryTextChange(text: String?): Boolean {
            searchValue = text ?: ""
            refreshSearch()
            return true
        }

    }

}