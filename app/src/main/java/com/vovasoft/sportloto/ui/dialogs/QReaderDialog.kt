package com.vovasoft.sportloto.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.vovasoft.qreader.QRDataListener
import com.vovasoft.qreader.QReader
import com.vovasoft.sportloto.R
import kotlinx.android.synthetic.main.dialog_q_reader.view.*
import android.util.DisplayMetrics
import android.view.View.MeasureSpec
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.LayoutInflater
import android.view.View
import android.os.Build
import android.support.v7.app.AlertDialog
import com.vovasoft.sportloto.R.id.view
import android.view.ViewTreeObserver
import com.vovasoft.sportloto.R.id.view






/***************************************************************************
 * Created by arseniy on 14/10/2017.
 ****************************************************************************/
class QReaderDialog(val context: Context) {

    private val qReaderDialogView: QReaderDialogView
    private var dialog: AlertDialog? = null

    private var onDetectedCallback: ((String)->Unit)? = null

    init {
        qReaderDialogView = QReaderDialogView(context)
    }


    fun show() {
        val builder = AlertDialog.Builder(context).setView(qReaderDialogView)
        dialog = builder.create()
        dialog?.show()
    }


    fun dismiss() {
        dialog?.dismiss()
    }


    fun setOnDetectedCallback(callback: (String)->Unit) {
        onDetectedCallback = callback
    }



    inner class QReaderDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : FrameLayout(context, attrs, defStyleAttr) {

        private var qReader: QReader? = null

        init {
            inflate(context, R.layout.dialog_q_reader, this)
            setupViews()
        }


        private fun setupViews() {

//            var obsereved = false
//
//            cameraView.viewTreeObserver.addOnGlobalLayoutListener({
//                if (!obsereved) {
//                    obsereved = true
//                    val width = cameraView.measuredWidth
//                    val height = cameraView.measuredHeight
//                    Log.e("Width", width.toString())
//                    Log.e("Height", height.toString())
//                }
//            })

            qReader = QReader.Builder(context, cameraView, QRDataListener { data ->
                onDetectedCallback?.let { onDetected ->
                    post({
                        onDetected(data)
                        dismiss()
                    })
                }
            }).facing(QReader.BACK_CAM)
                    .enableAutofocus(true)
                    .build()

            qReader?.start()

        }


        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
        }


        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            qReader?.initAndStart(cameraView)
        }


        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            qReader?.stop()
            qReader?.releaseAndCleanup()
        }

    }

}