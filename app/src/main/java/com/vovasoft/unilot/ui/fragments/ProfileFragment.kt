package com.vovasoft.unilot.ui.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.vovasoft.unilot.R
import com.vovasoft.unilot.components.AnimationUtils
import com.vovasoft.unilot.components.AppFragmentManager
import com.vovasoft.unilot.components.Preferences
import com.vovasoft.unilot.components.RevealAnimationSetting
import com.vovasoft.unilot.repository.models.entities.Wallet
import com.vovasoft.unilot.ui.dialogs.ProfileInfoDialog
import com.vovasoft.unilot.ui.recycler_adapters.WalletsRecyclerAdapter
import com.vovasoft.unilot.ui.widgets.ZxingReader
import com.vovasoft.unilot.view_models.GamesVM
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/***************************************************************************
 * Created by arseniy on 17/09/2017.
 ****************************************************************************/
class ProfileFragment : BaseFragment() {

    private lateinit var gamesVM: GamesVM

    private val walletsAdapter = WalletsRecyclerAdapter()
    private var hasCameraPermission = false

    companion object {
        fun newInstance(animationSettings: RevealAnimationSetting?): ProfileFragment {
            val fragment = ProfileFragment()

            val args = Bundle()
            args.putParcelable("animation", animationSettings)
            fragment.arguments = args

            return fragment
        }
    }

    private val animationSettings: RevealAnimationSetting?
        get() = arguments?.getParcelable("animation")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lockDrawerMode(true)

        animationSettings?.let {
            AnimationUtils.registerCircularRevealAnimation(context, view, it)
        }

        gamesVM = ViewModelProviders.of(activity).get(GamesVM::class.java)

        context?.let { context ->
            hasCameraPermission = checkSelfPermission(context, ZxingReader.CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED
        }

        setupViews()
        observeData()
    }

    private fun setupViews() {
        context?.let { context ->
            backBtn.setOnClickListener {
                onBackPressed()
            }

            infoBtn.setOnClickListener {
                val dialog = ProfileInfoDialog(context)
                dialog.show()
            }

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = walletsAdapter

            walletsAdapter.setOnDeleteListener { wallet ->
                val dialog = AlertDialog.Builder(context)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_confirmation)
                        .setNegativeButton(R.string.no, { dialog, _ ->
                            dialog.dismiss()
                        })
                        .setPositiveButton(R.string.yes, {dialog, _ ->
                            walletsAdapter.deleteWallet(wallet)
                            dialog.dismiss()
                        })
                        .create()
                dialog.show()
            }

            scanBtn.setOnClickListener {
                if (hasCameraPermission) {
                    runQReader()
                } else {
                    requestPermissions(listOf(ZxingReader.CAMERA_PERMISSION).toTypedArray(), ZxingReader.CAMERA_PERMISSION_CODE)
                }
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
    }


    private fun runQReader() {
        val intent = Intent(context, ZxingReader::class.java)
        startActivityForResult(intent, ZxingReader.RESULT_CODE)
    }


    private fun addWallet() {
        context?.let { context ->
            val walletNumber = walletEt.text.toString().trim()
            if (walletNumber.isNotEmpty() && !walletNumber.matches(Regex("^(0x)?[0-9a-f]{40}$"))) {

                val wallet = Wallet()
                wallet.number = walletNumber
                doAsync {
                    wallet.save()
                    uiThread {
                        walletEt.setText("")
                        walletEt.clearFocus()
                        activity.hideKeyboard()
                        walletsAdapter.addWallet(wallet)
                    }
                }

                Answers.getInstance().logCustom(CustomEvent("EVENT_WALLET_ADD")
                        .putCustomAttribute("language", Preferences.instance.language))
            }
            else {
                val alertDialog = AlertDialog.Builder(context).create()
                alertDialog.setTitle(getString(R.string.attention))
                alertDialog.setMessage(getString(R.string.invalid_wallet_number))
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                alertDialog.show()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ZxingReader.RESULT_CODE -> {
                    walletEt.setText(data?.getStringExtra("result"))
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        context?.let { context ->
            when (requestCode) {
                ZxingReader.CAMERA_PERMISSION_CODE -> {
                    hasCameraPermission = checkSelfPermission(context, ZxingReader.CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED
                    if (hasCameraPermission) {
                        runQReader()
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        lockDrawerMode(false)
        AppFragmentManager.instance.popBackStack()
    }

}
