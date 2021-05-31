package ca.isucorp.acme.ui.login

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivitySignUpBinding
import ca.isucorp.acme.util.*
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Activity where the user signs up
 */
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: LoginSignupViewModel by lazy {
        ViewModelProvider(this, LoginSignupViewModel.Factory(application)).get(LoginSignupViewModel::class.java)
    }

    /**
     * Called when the activity is starting.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolBarTitle = binding.layoutSimpleAppBar.toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.create_account)
        binding.layoutSimpleAppBar.toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)

        validateUserFields(binding.layoutActivitySignUpContent.root, this, viewModel, isSignUpScreen = true)

        binding.layoutActivitySignUpContent.buttonLogin.setOnClickListener {
            viewModel.register(binding.layoutActivitySignUpContent.textInputUserName.editUserName.text.toString(),
                binding.layoutActivitySignUpContent.textInputPassword.editPassword.text.toString())
        }

        viewModel.isSigningSuccessful.observe(this, {
            if (it == true) {
                MaterialDialog(this)
                    .title(text = getString(R.string.sign_up_successful))
                    .message(text = getString(R.string.sign_up_successful_message))
                    .cancelable(false)
                    .positiveButton(R.string.accept) {
                        finish()
                    }
                    .show()
            } /* //In a real-life project, database error should be handled further.
            else {
                MaterialDialog(this)
                    .title(text = getString(R.string.sign_up_error))
                    .message(text = getString(R.string.sign_up_error_message))
                    .positiveButton(R.string.accept) {}
                    .show()
            }*/
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
    }
}