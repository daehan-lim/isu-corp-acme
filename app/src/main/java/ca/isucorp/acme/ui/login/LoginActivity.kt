package ca.isucorp.acme.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityLoginBinding
import ca.isucorp.acme.util.underlineText


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var signUpTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpTextView = binding.layoutActivityLoginContent.findViewById(R.id.text_sign_up)
        signUpTextView.underlineText()
        signUpTextView.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
    }
}