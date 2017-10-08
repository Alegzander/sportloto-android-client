package com.vovasoft.sportloto.ui.widgets.DaysBoardView

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.sportloto.R
import kotlinx.android.synthetic.main.view_white_tab.view.*

/***************************************************************************
 * Created by arseniy on 08/10/2017.
 ****************************************************************************/
class WhiteTabView : FrameLayout {


    init {
        inflate(context, R.layout.view_white_tab, this)
    }


    constructor(context: Context) : super(context)


    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)


    fun setDigit(charDigit: Char) {
        digit.text = charDigit.toString()
    }

}