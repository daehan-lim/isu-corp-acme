package ca.isucorp.acme.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityLoginBinding
import ca.isucorp.acme.util.afterTextChanged
import ca.isucorp.acme.util.underlineText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var signUpTextView: TextView
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, LoginViewModel.Factory(application)).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpTextView = binding.layoutActivityLoginContent.findViewById(R.id.text_sign_up)
        signUpTextView.underlineText()
        signUpTextView.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }

        val usernameTextInput = binding.layoutActivityLoginContent.findViewById<TextInputLayout>(R.id.text_input_user_name)
        val passwordTextInput = binding.layoutActivityLoginContent.findViewById<TextInputLayout>(R.id.text_input_password)
        val usernameEditText = binding.layoutActivityLoginContent.findViewById<TextInputEditText>(R.id.edit_user_name)
        val passwordEditText = binding.layoutActivityLoginContent.findViewById<TextInputEditText>(R.id.edit_password)

        viewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password are valid
            binding.layoutActivityLoginContent.findViewById<MaterialButton>(R.id.button_login).isEnabled = loginState.isDataValid

            usernameTextInput.error = when(loginState.usernameError) {
                null -> null
                else -> getString(loginState.usernameError)
            }

            passwordTextInput.error = when(loginState.passwordError) {
                null -> null
                else -> getString(loginState.passwordError)
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
}