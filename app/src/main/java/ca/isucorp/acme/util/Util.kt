package ca.isucorp.acme.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.text.style.RelativeSizeSpan
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import ca.isucorp.acme.R
import ca.isucorp.acme.ui.directions.GetDirectionsActivity
import ca.isucorp.acme.ui.login.LoginSignupViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


/**
 * Default animation id for pressing back button in Activity
 */
const val DEFAULT_GO_BACK_ANIMATION = R.anim.slide_out_right


/**
 * Date pattern to use with Date objects showing the date (in one or two characters), month (in three characters) and the whole year
 * Example: 5 Jan 2021
 */
const val DAY_SHORT_MONTH_YEAR_PATTERN = "d MMM yyyy"

/**
 * Pattern to use with Date objects showing hour (in one or two characters), minute and time period
 * Example: 05:25 PM
 */
const val TIME_PATTERN = "h:mm a"

/**
 * Pattern to show date and time combining DAY_SHORT_MONTH_YEAR_PATTERN and TIME_PATTERN
 */
const val SHORT_DATE_AND_TIME_PATTERN = "$DAY_SHORT_MONTH_YEAR_PATTERN, $TIME_PATTERN"

/**
 * Pattern to show full date and time
 * Example: Saturday, Dec 24, 2016  11:35 AM
 */
const val FULL_DATE_AND_TIME_PATTERN = "EEE, MMM d, yyyy  $TIME_PATTERN"

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
    setNavigationIcon(R.drawable.ic_arrow_left_back)
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

fun validateUserFields(parentLayout: View, activity: AppCompatActivity, viewModel: LoginSignupViewModel, isSignUpScreen: Boolean = false) {
    val usernameTextInput = parentLayout.findViewById<TextInputLayout>(R.id.text_input_user_name)
    val passwordTextInput = parentLayout.findViewById<TextInputLayout>(R.id.text_input_password)
    val usernameEditText = parentLayout.findViewById<TextInputEditText>(R.id.edit_user_name)
    val passwordEditText = parentLayout.findViewById<TextInputEditText>(R.id.edit_password)

    var secondPasswordTextInput: TextInputLayout? = null
    var secondPasswordEditText: TextInputEditText? = null

    viewModel.loginSignUpFormState.observe(activity, Observer {
        val loginState = it ?: return@Observer

        // disable login button unless both username / password are valid
        parentLayout.findViewById<MaterialButton>(R.id.button_login).isEnabled = loginState.isDataValid

        usernameTextInput.error = when(loginState.usernameError) {
            null -> null
            else -> activity.getString(loginState.usernameError)
        }

        passwordTextInput.error = when(loginState.passwordError) {
            null -> null
            else -> activity.getString(loginState.passwordError)
        }

        if(isSignUpScreen) {
            secondPasswordTextInput?.error = when(loginState.secondPasswordError) {
                null -> null
                else -> activity.getString(loginState.secondPasswordError)
            }
        }
    })

    if(isSignUpScreen) {
        secondPasswordTextInput = parentLayout.findViewById(R.id.text_input_repeat_password)
        secondPasswordEditText = parentLayout.findViewById(R.id.edit_repeat_password)
        secondPasswordEditText.afterTextChanged {
            viewModel.signupDataChanged(usernameEditText.text.toString(), passwordEditText.text.toString(),
                secondPasswordEditText.text.toString())
        }

        usernameEditText.afterTextChanged {
            viewModel.signupDataChanged(usernameEditText.text.toString(), passwordEditText.text.toString(),
                secondPasswordEditText.text.toString())
        }

        passwordEditText.afterTextChanged {
            viewModel.signupDataChanged(usernameEditText.text.toString(), passwordEditText.text.toString(),
                secondPasswordEditText.text.toString())
        }
    } else {
        usernameEditText.afterTextChanged {
            viewModel.loginDataChanged(usernameEditText.text.toString(), passwordEditText.text.toString())
        }

        passwordEditText.afterTextChanged {
            viewModel.loginDataChanged(usernameEditText.text.toString(), passwordEditText.text.toString())
        }
    }

}

/**
 * It inflates  a [PopupMenu] and increases its items' text size if called from a tablet
 * @param popup The [PopupMenu] to be inflated
 * @param isTabletSize true if the device is a tablet
 */
fun inflateDropdownMenu(popup: PopupMenu, isTabletSize: Boolean) {
    popup.inflate(R.menu.dropdown_menu)
    if(isTabletSize) {
        increaseMenuItemTextSize(popup, R.id.action_work_ticker)
        increaseMenuItemTextSize(popup, R.id.action_get_directions)
        increaseMenuItemTextSize(popup, R.id.action_dashboard)
    }
}

/**
 * Increases text size of the dropdown menu items by 50%.
 * @param popupMenu The menu where the items are
 * @param menuItemId The id of the menu item whose size is to be increased
 */
fun increaseMenuItemTextSize(popupMenu: PopupMenu, menuItemId: Int) {
    val menuItem = popupMenu.menu.findItem(menuItemId)
    val spanString = SpannableString(menuItem.title.toString())
    val end = spanString.length
    spanString.setSpan(RelativeSizeSpan(1.5f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    menuItem.title = spanString
}

fun convertToLocalDateTime(date: Date?): LocalDateTime {
    return Instant.ofEpochMilli(date?.time ?: Calendar.getInstance().timeInMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

/**
 * If this [TextView] is placed inside [ScrollView] it will be allowed to scroll inside it
 */
fun TextView.makeScrollableInsideScrollView() {
    movementMethod = ScrollingMovementMethod()
    setOnTouchListener { v, event ->
        v.parent.requestDisallowInterceptTouchEvent(true)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> {
                v.parent.requestDisallowInterceptTouchEvent(false)
                //It is required to call performClick() in onTouch event.
                performClick()
            }
        }
        false
    }
}

fun showDropdownMenu(popup: PopupMenu, activity: AppCompatActivity, isTabletSize: Boolean) {
    popup.inflate(R.menu.dropdown_menu)
    if(isTabletSize) {
        increaseMenuItemTextSize(popup, R.id.action_work_ticker)
        increaseMenuItemTextSize(popup, R.id.action_get_directions)
        increaseMenuItemTextSize(popup, R.id.action_dashboard)
    }

    popup.show()
}