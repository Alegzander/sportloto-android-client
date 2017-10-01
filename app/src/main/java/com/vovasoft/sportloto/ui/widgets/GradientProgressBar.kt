package com.vovasoft.sportloto.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.sportloto.R
import kotlinx.android.synthetic.main.gradient_progress_bar.view.*



/***************************************************************************
 * Created by arseniy on 01/10/2017.
 ****************************************************************************/
class GradientProgressBar : FrameLayout {

    init {
        inflate(context, R.layout.gradient_progress_bar, this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    fun setProgress(progress: Int) {
        val parentWidth = progressRoot.measuredWidth - progressRoot.paddingLeft - progressRoot.paddingRight
        progressView.layoutParams.width = (parentWidth * progress) / 100
        progressView.requestLayout()
    }
}