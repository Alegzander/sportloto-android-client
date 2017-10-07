package com.vovasoft.sportloto.ui.widgets.PrizeBoardView

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.sportloto.R
import kotlinx.android.synthetic.main.view_black_tab.view.*

/***************************************************************************
 * Created by arseniy on 07/10/2017.
 ****************************************************************************/
class BlackTabView : FrameLayout {


    init {
        inflate(context, R.layout.view_black_tab, this)
    }


    constructor(context: Context) : super(context)


    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)


    fun setDigit(charDigit: Char) {
        digit.text = charDigit.toString()
    }

}