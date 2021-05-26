package ca.isucorp.acme.util

import android.text.SpannableString
import android.text.style.UnderlineSpan
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
    setNavigationIcon(R.drawable.ic_arrow_circle_left_back)
    setNavigationOnClickListener {
        goBackWithAnimation(activity, goBackAnimation)
    }
}