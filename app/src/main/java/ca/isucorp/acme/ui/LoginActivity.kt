package ca.isucorp.acme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}