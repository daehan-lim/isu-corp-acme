package ca.isucorp.acme.ui.login

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivitySignUpBinding
import ca.isucorp.acme.util.*
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, LoginViewModel.Factory(application)).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.layoutSimpleAppBar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val toolBarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.create_account)
        toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)

        validateUserFields(binding.layoutActivitySignUpContent, this, viewModel, isSignUpScreen = true)

        val usernameEditText = binding.layoutActivitySignUpContent.findViewById<TextInputEditText>(R.id.edit_user_name)
        val passwordEditText = binding.layoutActivitySignUpContent.findViewById<TextInputEditText>(R.id.edit_password)

        binding.layoutActivitySignUpContent.findViewById<MaterialButton>(R.id.button_login).setOnClickListener {
            viewModel.register(usernameEditText.text.toString(), passwordEditText.text.toString())
        }

        viewModel.isSigningSuccessful.observe(this, {
            if(it) {
                MaterialDialog(this)
                    .title(text = getString(R.string.sign_up_successful))
                    .message(text = getString(R.string.sign_up_successful_message))
                    .cancelable(false)
                    .positiveButton(R.string.accept) {
                        /*val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)*/
                        finish()
                    }
                    .show()

            } else {
                MaterialDialog(this)
                    .title(text = getString(R.string.sign_up_error))
                    .message(text = getString(R.string.sign_up_error_message))
                    .positiveButton(R.string.accept) {}
                    .show()
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
    }
}