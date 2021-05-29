package ca.isucorp.acme.ui.workticket

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityWorkTicketBinding
import ca.isucorp.acme.util.*
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class WorkTicketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.myNavHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        val toolbar = binding.layoutSimpleAppBar.toolbar
        val toolBarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.work_ticket)
        toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)

        setBottomImageLogoCenter()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener{
            Handler(Looper.getMainLooper()).post {
                setBottomImageLogoCenter()
            }
            true
        }

    }

    private fun setBottomImageLogoCenter() {
        val cameraItemView = binding.bottomNavigationView.findViewById<View>(R.id.nav_camera)
        val cameraImageView = cameraItemView.findViewById(R.id.icon) as ImageView
        val params = cameraImageView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER
    }

    /*private fun removeBottomNavViewTextAndCenter(sectionItem: View) {
        val largeTextView: TextView = sectionItem.findViewById(com.google.android.material.R.id.largeLabel)
        val smallTextView: TextView = sectionItem.findViewById(com.google.android.material.R.id.smallLabel)
        smallTextView.gravity = Gravity.CENTER
        largeTextView.gravity = Gravity.CENTER
        *//*smallTextView.visibility = View.GONE
        largeTextView.visibility = View.GONE*//*
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
    }
}