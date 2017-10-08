package com.vovasoft.sportloto.ui.widgets.PrizeBoardView

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.vovasoft.sportloto.R
import kotlinx.android.synthetic.main.view_prize_board.view.*

/***************************************************************************
 * Created by arseniy on 07/10/2017.
 ****************************************************************************/
class PrizeBoardView : FrameLayout {

    private val integerTabs: MutableList<BlackTabView> = mutableListOf()
    private val fractionTabs: MutableList<BlackTabView> = mutableListOf()


    init {
        inflate(context, R.layout.view_prize_board, this)

        integerTabs.add(integer1)
        integerTabs.add(integer10)

        fractionTabs.add(fraction1)
        fractionTabs.add(fraction10)
        fractionTabs.add(fraction100)
    }


    constructor(context: Context) : super(context)


    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)


    fun setValue(value: String) {
        val digitsValue = value.replace(",", ".")
        val digits = digitsValue.split(".").toMutableList()

        var integers = ""
        for (i in 1..2-digits[0].length) {
            integers = "0$integers"
        }
        digits[0] = "$integers${digits[0]}"

        var fractions = ""
        for (i in 1..3-digits[1].length) {
            fractions = "${fractions}0"
        }
        digits[1] = "${digits[1]}$fractions"

        integerTabs.forEachIndexed { index, blackTabView ->
            blackTabView.setDigit(digits[0][index])
        }

        fractionTabs.forEachIndexed { index, blackTabView ->
            blackTabView.setDigit(digits[1][index])
        }
    }
}