package ca.isucorp.acme.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.isucorp.acme.R
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.databinding.ActivityMainBinding
import ca.isucorp.acme.ui.login.SignUpActivity
import ca.isucorp.acme.ui.newticket.NewTicketActivity
import ca.isucorp.acme.util.increaseMenuItemTextSize
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


const val PHONE_PERMISSION_CODE = 101
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isTabletSize: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.layoutMainAppBar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        isTabletSize = resources.getBoolean(R.bool.isTablet)
        if(isTabletSize) {
            toolbar.layoutParams.height = resources.getDimension(R.dimen.action_bar_size).toInt()
        }

        val menuButton = toolbar.findViewById<ImageView>(R.id.button_menu)
        menuButton.setOnClickListener {
            showDropdownMenu(menuButton)
        }

        toolbar.findViewById<ImageView>(R.id.button_new_ticket).setOnClickListener {
            startActivity(Intent(applicationContext, NewTicketActivity::class.java))
        }

        val adapter = TicketsAdapter(TicketsAdapter.CallListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))
            startActivity(callIntent)
        }, TicketsAdapter.ViewDetailsListener {
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] += 40
            val maxYear = calendar.timeInMillis
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText(getString(R.string.select_date))
                .setCalendarConstraints(constraintsBuilder.build())
                .setTheme(R.style.MaterialCalendarTheme)
                .build()
                .show(supportFragmentManager, "date")
        })
        binding.recyclerView.adapter = adapter

        adapter.data = listOf(
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", Date(), "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", Date(),"542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", Date(), "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", Date(), "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", Date(),"542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", Date(), "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", Date(), "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", Date(),"542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", Date(), "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", Date(), "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", Date(),"542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", Date(), "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", Date(), "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", Date(),"542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", Date(), "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", Date(), "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", Date(),"542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", Date(), "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", Date(), "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", Date(),"542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", Date(), "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", Date(), "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", Date(),"542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", Date(), "519 733 8727"),
        )

        /*viewModel.comments.observe(fragment.viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                adapter.data = ArrayList(it)
                adapter.notifyDataSetChanged()
            } else {
                adapter.data = ArrayList()
                recyclerView.visibility = View.GONE
                noCommentsLayout.visibility = View.VISIBLE
            }
        })

        viewModel.mainError.observe(fragment.viewLifecycleOwner, {
            if (it) {
                loadingAnimationView.visibility = View.GONE
                if(scrollView != null) {
                    scrollView!!.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.GONE
                }
                adapter.data = ArrayList()
                noConnectionLayout.visibility = View.VISIBLE
                viewModel.resetMainError()
            }
        })*/
    }

    private fun showDropdownMenu(menuButton: ImageView?) {
        val popup = PopupMenu(this, menuButton)
        popup.inflate(R.menu.dropdown_menu)

        if(isTabletSize) {
            increaseMenuItemTextSize(popup, R.id.action_work_ticker)
            increaseMenuItemTextSize(popup, R.id.action_get_directions)
        }

        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_work_ticker -> {
                    Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show()
                }
                R.id.action_get_directions -> {
                    Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        popup.show()
    }




}