package com.mahendri.pasbeli.util

import android.widget.AutoCompleteTextView

/**
 * @author Mahendri
 */
class AutoValidator(val wordList: List<String>) : AutoCompleteTextView.Validator {

    override fun fixText(invalidText: CharSequence?): CharSequence {
        return ""
    }

    override fun isValid(text: CharSequence?): Boolean {
        return wordList.contains(text.toString())
    }
}