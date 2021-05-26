package ca.isucorp.acme.util

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView

/**
 * Sets a span to underline the TextView's text
 */
fun TextView.underlineText() {
    val content = SpannableString(text.toString())
    content.setSpan(UnderlineSpan(), 0, text.toString().length, 0)
    text = content
}