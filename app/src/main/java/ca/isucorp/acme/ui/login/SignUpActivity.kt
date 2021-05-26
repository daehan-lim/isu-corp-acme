package ca.isucorp.acme.ui.login

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityLoginBinding
import ca.isucorp.acme.databinding.ActivitySignUpBinding
import ca.isucorp.acme.util.DEFAULT_GO_BACK_ANIMATION
import ca.isucorp.acme.util.goBackWithAnimation
import ca.isucorp.acme.util.setUpInActivity
import ca.isucorp.acme.util.underlineText


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.layoutAppBar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val toolBarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.create_account)
        toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)



    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
    }
}