package com.vovasoft.sportloto.ui.widgets

import android.Manifest
import android.app.Activity
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.zxing.Result
import android.content.Intent




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
        // If you would like to resume scanning, call this method below:
        // scannerView.resumeCameraPreview(this);
    }
}
