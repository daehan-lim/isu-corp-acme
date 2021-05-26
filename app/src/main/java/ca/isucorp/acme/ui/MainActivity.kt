package ca.isucorp.acme.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityMainBinding
import java.util.*


const val PHONE_PERMISSION_CODE = 102
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


/*//        setSupportActionBar(binding.layoutAppBar)
        supportActionBar?.elevation = 0F
        supportActionBar?.title = getString(R.string.app_name)*/
    }



}