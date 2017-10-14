package com.vovasoft.sportloto.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vovasoft.sportloto.R
import com.vovasoft.sportloto.repository.models.Wallet
import com.vovasoft.sportloto.ui.recycler_adapters.WalletsRecyclerAdapter
import com.vovasoft.sportloto.view_models.GamesVM
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.view.inputmethod.EditorInfo
import android.widget.TextView




/***************************************************************************
 * Created by arseniy on 17/09/2017.
 ****************************************************************************/
class ProfileFragment : BaseFragment() {

    private val gamesVM: GamesVM
        get() = ViewModelProviders.of(activity).get(GamesVM::class.java)


    private val walletsAdapter = WalletsRecyclerAdapter()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeData()
    }


    private fun setupViews() {

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = walletsAdapter

        scanBtn.setOnClickListener {

        }

        walletEt.setOnClickListener {
            walletEt.isCursorVisible = true
        }

        walletEt.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addWallet()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })

        addWalletBtn.setOnClickListener {
            addWallet()
        }
    }


    private fun addWallet() {
        if (walletEt.text.toString().isNotEmpty()) {
            val wallet = Wallet()
            wallet.number = walletEt.text.toString()
            doAsync {
                wallet.save()
                uiThread {
                    walletEt.setText("")
                    walletEt.isCursorVisible = false
                    activity.hideKeyboard()
                    walletsAdapter.addWallet(wallet)
                }
            }
        }
    }


    private fun observeData() {
        gamesVM.getWallets().observe(this, Observer { wallets ->
            wallets?.let {
                walletsAdapter.addWallets(wallets)
            }
        })
    }

}
