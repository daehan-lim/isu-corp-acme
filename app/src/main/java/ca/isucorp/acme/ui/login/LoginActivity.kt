package ca.isucorp.acme.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityLoginBinding
import ca.isucorp.acme.ui.dashboard.MainActivity
import ca.isucorp.acme.util.underlineText
import ca.isucorp.acme.util.validateUserFields
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var signUpTextView: TextView
    private val viewModel: LoginSignupViewModel by lazy {
        ViewModelProvider(this, LoginSignupViewModel.Factory(application)).get(LoginSignupViewModel::class.java)
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

        validateUserFields(binding.layoutActivityLoginContent, this, viewModel)

        val usernameEditText = binding.layoutActivityLoginContent.findViewById<TextInputEditText>(R.id.edit_user_name)
        val passwordEditText = binding.layoutActivityLoginContent.findViewById<TextInputEditText>(R.id.edit_password)

        binding.layoutActivityLoginContent.findViewById<MaterialButton>(R.id.button_login).setOnClickListener {
            viewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
        }

        viewModel.isSigningSuccessful.observe(this, {
            if(it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                MaterialDialog(this)
                    .title(text = getString(R.string.sign_in_error))
                    .message(text = getString(R.string.sign_in_error_message))
                    .positiveButton(R.string.accept) {}
                    .show()
            }
        })
    }
}