package ca.isucorp.acme.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityLoginBinding
import ca.isucorp.acme.ui.dashboard.MainActivity
import ca.isucorp.acme.util.underlineText
import ca.isucorp.acme.util.validateUserFields
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Login screen, which is the first screen shown in the app
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginSignupViewModel by lazy {
        ViewModelProvider(this, LoginSignupViewModel.Factory(application)).get(LoginSignupViewModel::class.java)
    }

    /**
     * Called when the activity is starting.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutActivityLoginContent.textSignUp.underlineText()
        binding.layoutActivityLoginContent.textSignUp.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }

        validateUserFields(binding.layoutActivityLoginContent.root, this, viewModel)

        binding.layoutActivityLoginContent.buttonLogin.setOnClickListener {
                viewModel.login(binding.layoutActivityLoginContent.textInputUserName.editUserName.text.toString(),
                    binding.layoutActivityLoginContent.textInputPassword.editPassword.text.toString())
            }

        viewModel.isSigningSuccessful.observe(this, {
            if (it != null) {
                viewModel.handledSigning()
                if (it == true) {
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
            }
        })
    }
}