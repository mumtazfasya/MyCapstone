package com.dicoding.myapplication

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class PasswordEditText : AppCompatEditText {
    private lateinit var errorIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        errorIcon = ContextCompat.getDrawable(context, R.drawable.baseline_error_24) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val password = p0.toString().trim()
                if (password.isNotEmpty() && password.length < 8) {
                    showError("Min. 8 characters")
                } else {
                    removeError()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun showError(errorMessage: String) {
        error = errorMessage
        setErrorIconVisible(true)
    }

    private fun removeError() {
        error = null
        setErrorIconVisible(false)
    }

    private fun setErrorIconVisible(visible: Boolean) {
        val compoundDrawables = compoundDrawablesRelative
        val errorIconDrawable = if (visible) errorIcon else null
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            compoundDrawables[0],
            compoundDrawables[1],
            errorIconDrawable,
            compoundDrawables[3]
        )
    }
}