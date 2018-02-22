package com.vovasoft.unilot.ui.widgets

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import kotlinx.android.synthetic.main.gsme_progres.view.*


/***************************************************************************
 * Created by arseniy on 01/10/2017.
 ****************************************************************************/
class GameProgress : FrameLayout {

    init {
        inflate(context, R.layout.gsme_progres, this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    fun setProgress(progress: Int) {
        val parentWidth = progressRoot.measuredWidth - progressRoot.paddingLeft - progressRoot.paddingRight
        progressView.layoutParams.width = (parentWidth * progress) / 100
        progressView.requestLayout()
    }


    fun inversColor(inverse: Boolean) {
        if (inverse) {
            progressRoot.background = ContextCompat.getDrawable(context, R.drawable.view_progress_background_inverse)
            progressView.background = ContextCompat.getDrawable(context, R.drawable.view_progress_foreground_inverse)
        }
        else {
            progressRoot.background = ContextCompat.getDrawable(context, R.drawable.view_progress_background)
            progressView.background = ContextCompat.getDrawable(context, R.drawable.view_progress_foreground)
        }
    }
}