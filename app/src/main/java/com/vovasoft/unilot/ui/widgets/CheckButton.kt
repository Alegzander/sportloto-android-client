package com.vovasoft.unilot.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.vovasoft.unilot.R
import kotlinx.android.synthetic.main.view_check_button.view.*

/***************************************************************************
 * Created by arseniy on 08/10/2017.
 ****************************************************************************/
class CheckButton : FrameLayout {

    init {
        inflate(context, R.layout.view_check_button, this)
        checkBox.visibility = View.INVISIBLE
    }


    constructor(context: Context) : super(context)


    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        initWithAttrs(attrs)
    }


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initWithAttrs(attrs)
    }


    private fun initWithAttrs(attrs: AttributeSet?) {
        val viewAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.CheckButton, 0, 0)
        textView.text = viewAttrs.getText(R.styleable.CheckButton_btnText)
    }


    var text: String = ""
        set(value) {
            field = value
            textView.text = field
        }


    var isChecked: Boolean = false
        set(value) {
            if (value) {
                checkBox.visibility = View.VISIBLE
            }
            else {
                checkBox.visibility = View.INVISIBLE
            }
            field = value
        }


    fun setOnCheckListener(onCheck: (Boolean) -> Unit) {
        setOnClickListener({
            isChecked = !isChecked
            onCheck(isChecked)
        })
    }

}