package ca.isucorp.acme.ui.workticket

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ca.isucorp.acme.R
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.databinding.ActivityWorkTicketBinding
import ca.isucorp.acme.ui.dashboard.EXTRA_TICKET
import ca.isucorp.acme.ui.directions.GetDirectionsActivity
import ca.isucorp.acme.ui.editticket.EditTicketActivity
import ca.isucorp.acme.util.*


class WorkTicketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkTicketBinding
    private var isTabletSize: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.myNavHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        val toolbar = binding.layoutWorkTicketAppBar.toolbar
        val toolBarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.work_ticket)
        setSupportActionBar(toolbar)
        title = ""

        val ticket = (intent?.getSerializableExtra(EXTRA_TICKET) as? Ticket).apply {
            if (this == null) {
                finish()
                return
            }
        }!!

        val editTicketButton = toolbar.findViewById<ImageView>(R.id.button_edit_ticket)
        val dropdownMenuButton = toolbar.findViewById<ImageView>(R.id.button_menu)
        val backMenuButton = toolbar.findViewById<ImageView>(R.id.back_menu_button)

        editTicketButton.visibility = View.VISIBLE
        dropdownMenuButton.visibility = View.VISIBLE

        isTabletSize = resources.getBoolean(R.bool.isTablet)

        backMenuButton.visibility = View.VISIBLE
        backMenuButton.setOnClickListener {
            goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
        }

        dropdownMenuButton.setOnClickListener {
            val popup = PopupMenu(this, editTicketButton)
            inflateDropdownMenu(popup, isTabletSize)
            popup.menu.findItem(R.id.action_work_ticker).isVisible = false
            popup.menu.findItem(R.id.action_dashboard).isVisible = true
            popup.setOnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.action_dashboard -> {
                        finish()
                        goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
                    }
                    R.id.action_get_directions -> {
                        startActivity(Intent(applicationContext, GetDirectionsActivity::class.java).apply {
                            putExtra(EXTRA_ADDRESS, ticket.address)
                        })
                    }
                }
                true
            }
            popup.show()
        }


        editTicketButton.setOnClickListener {
            finish()
            startActivity(Intent(applicationContext, EditTicketActivity::class.java).apply {
                putExtra(EXTRA_TICKET, ticket)
            })
        }

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