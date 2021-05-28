package ca.isucorp.acme.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityMainBinding
import ca.isucorp.acme.ui.newticket.NewTicketActivity
import ca.isucorp.acme.util.increaseMenuItemTextSize
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.Factory(application)).get(MainViewModel::class.java)
    }



    private var isTabletSize: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.layoutMainAppBar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val hamburgerMenuIcon = toolbar.findViewById<ImageView>(R.id.button_menu)
        val newTicketBarButton = toolbar.findViewById<ImageView>(R.id.button_new_ticket)
        val calendarButton = toolbar.findViewById<ImageView>(R.id.button_calendar)
        val calendarSyncButton = toolbar.findViewById<ImageView>(R.id.button_sync_calendar)

        TooltipCompat.setTooltipText(hamburgerMenuIcon, getString(R.string.menu))
        TooltipCompat.setTooltipText(newTicketBarButton, getString(R.string.new_ticket))
        TooltipCompat.setTooltipText(calendarButton, getString(R.string.ticket_calendar))
        TooltipCompat.setTooltipText(calendarSyncButton, getString(R.string.sync_calendar))


        isTabletSize = resources.getBoolean(R.bool.isTablet)
        if(isTabletSize) {
            toolbar.layoutParams.height = resources.getDimension(R.dimen.action_bar_size).toInt()
        }

        hamburgerMenuIcon.setOnClickListener {
            showDropdownMenu(hamburgerMenuIcon)
        }

        newTicketBarButton.setOnClickListener {
            startActivity(Intent(applicationContext, NewTicketActivity::class.java))
        }

        val adapter = TicketsAdapter(TicketsAdapter.CallListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))
            startActivity(callIntent)
        }, TicketsAdapter.ViewDetailsListener {

        })
        binding.recyclerView.adapter = adapter

        viewModel.tickets.observe(this, {
            if(it.isNotEmpty()) {
                binding.layoutNoResults.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(it)
            } else {
                binding.layoutNoResults.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        })

        /*adapter.data = listOf(
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", "28 May 2021, 5:43 AM","542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", "28 May 2021, 5:43 AM","542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", "28 May 2021, 5:43 AM","542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", "28 May 2021, 5:43 AM","542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", "28 May 2021, 5:43 AM","542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", "28 May 2021, 5:43 AM","542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", "28 May 2021, 5:43 AM","542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Sink Repair", "37 Greennight Cres Waterloo, ON N2R 4K8", "28 May 2021, 5:43 AM", "519 733 8727"),
            Ticket("Water Heater Installation", "11 Westnight Ave Toronto On, N7L 1X1", "28 May 2021, 5:43 AM","542 332 4932"),
            Ticket("Drain Cleaning", "7 Hedgestill Street Guelph, ON N2D 7L0", "28 May 2021, 5:43 AM", "519 733 8727"),
        )*/
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