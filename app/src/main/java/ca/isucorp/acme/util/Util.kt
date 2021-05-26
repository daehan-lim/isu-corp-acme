package ca.isucorp.acme.util

import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import ca.isucorp.acme.R
import ca.isucorp.acme.ui.login.LoginViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


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

fun validateUserFields(parentLayout: View, activity: AppCompatActivity, viewModel: LoginViewModel) {
    val usernameTextInput = parentLayout.findViewById<TextInputLayout>(R.id.text_input_user_name)
    val passwordTextInput = parentLayout.findViewById<TextInputLayout>(R.id.text_input_password)
    val usernameEditText = parentLayout.findViewById<TextInputEditText>(R.id.edit_user_name)
    val passwordEditText = parentLayout.findViewById<TextInputEditText>(R.id.edit_password)

    viewModel.loginFormState.observe(activity, Observer {
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
    })

    usernameEditText.afterTextChanged {
        viewModel.loginDataChanged(
            usernameEditText.text.toString(),
            passwordEditText.text.toString()
        )
    }

    passwordEditText.apply {
        afterTextChanged {
            viewModel.loginDataChanged(usernameEditText.text.toString(), passwordEditText.text.toString())
        }

        /*setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    viewModel.login(username.text.toString(), password.text.toString())
            }
            false
        }*/

        /*login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.login(username.text.toString(), password.text.toString())
        }*/
    }
}