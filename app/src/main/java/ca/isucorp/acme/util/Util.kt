package ca.isucorp.acme.util

import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ca.isucorp.acme.R


/**
 * Default animation id for pressing back button in Activity
 */
const val DEFAULT_GO_BACK_ANIMATION = R.anim.slide_out_right

/**
 * Sets a span to underline the TextView's text
 */
fun TextView.underlineText() {
    val content = SpannableString(text.toString())
    content.setSpan(UnderlineSpan(), 0, text.toString().length, 0)
    text = content
}


fun goBackWithAnimation(activity: AppCompatActivity, anim: Int?) {
    activity.finish()
    if(anim != null) {
        activity.overridePendingTransition(0, anim)
    }
}

fun Toolbar.setUpInActivity(activity: AppCompatActivity, goBackAnimation: Int?) {
    activity.setSupportActionBar(this)
    title = ""
    setNavigationIcon(R.drawable.ic_arrow__left_back)
    setNavigationOnClickListener {
        goBackWithAnimation(activity, goBackAnimation)
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}