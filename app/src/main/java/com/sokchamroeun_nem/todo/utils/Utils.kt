package com.sokchamroeun_nem.todo.utils

import android.graphics.Paint
import android.widget.TextView

object Utils {

    fun setChecked(text: TextView) {
        text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun removeChecked(text: TextView) {
        text.paintFlags = text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

}