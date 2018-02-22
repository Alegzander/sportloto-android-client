package com.vovasoft.unilot.ui.widgets

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


/***************************************************************************
 * Created by arseniy on 21/10/2017.
 ****************************************************************************/
class ZxingReader : AppCompatActivity(), ZXingScannerView.ResultHandler {

    companion object {
        val CAMERA_PERMISSION = Manifest.permission.CAMERA
        val CAMERA_PERMISSION_CODE = 100
        val RESULT_CODE = 101
    }

    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
        scannerView.setResultHandler(this)
    }

    public override fun onResume() {
        super.onResume()
        scannerView.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        val data = Intent()
        data.putExtra("result", rawResult.text)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
