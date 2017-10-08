package com.vovasoft.sportloto.ui.widgets.DaysBoardView

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.sportloto.R
import kotlinx.android.synthetic.main.view_days_board.view.*

/***************************************************************************
 * Created by arseniy on 08/10/2017.
 ****************************************************************************/
class DaysBoardView : FrameLayout {


    init {
        inflate(context, R.layout.view_days_board, this)
    }


    constructor(context: Context) : super(context)


    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)


    fun setValue(value: String) {
        integer10.setDigit(value[0])
        integer1.setDigit(value[1])
    }
}