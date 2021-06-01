package ca.isucorp.acme.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityMainBinding
import ca.isucorp.acme.ui.calendar.CalendarActivity
import ca.isucorp.acme.ui.directions.GetDirectionsActivity
import ca.isucorp.acme.ui.newticket.NewTicketActivity
import ca.isucorp.acme.ui.workticket.WorkTicketActivity
import ca.isucorp.acme.util.addEventToCalendar
import ca.isucorp.acme.util.inflateDropdownMenu
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.snackbar.Snackbar

const val WRITE_TO_CALENDAR_PERMISSION_CODE = 100
const val EXTRA_TICKET = "ca.isucorp.acme.ui.dashboard.MainActivity.TICKET"

/**
 * Main activity of the app containing the dashboard screen
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * Whether the app requested permission to read and write the calendar
     */
    private var calendarPermissionWasRequested = false

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.Factory(application)).get(MainViewModel::class.java)
    }

    /**
     * Indicates whether the the device is a tablet or not
     */
    private var isTabletSize: Boolean = false

    /**
     * Called when the activity is starting.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.layoutMainAppBar.toolbar
        setSupportActionBar(toolbar)

        val hamburgerMenuIcon = toolbar.findViewById<ImageView>(R.id.button_menu)
        val newTicketBarButton = toolbar.findViewById<ImageView>(R.id.button_new_ticket)
        val calendarButton = toolbar.findViewById<ImageView>(R.id.button_calendar)
        val calendarSyncButton = toolbar.findViewById<ImageView>(R.id.button_sync_calendar)

        TooltipCompat.setTooltipText(hamburgerMenuIcon, getString(R.string.menu))
        TooltipCompat.setTooltipText(newTicketBarButton, getString(R.string.new_ticket))
        TooltipCompat.setTooltipText(calendarButton, getString(R.string.ticket_calendar))
        TooltipCompat.setTooltipText(calendarSyncButton, getString(R.string.sync_calendar))

        calendarButton.setOnClickListener {
            startActivity(Intent(applicationContext, CalendarActivity::class.java))
        }


        viewModel.dueTickets.observe(this, {})

        calendarSyncButton.setOnClickListener {
            MaterialDialog(this)
                .title(text = getString(R.string.sync_with_calendar))
                .message(text = getString(R.string.sync_with_calendar_message))
                .positiveButton(R.string.accept) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
                        requestCalendarPermission()
                    } else {
                        syncToCalendar()
                    }
                }
                .negativeButton(R.string.cancel){}
                .show()
        }

        isTabletSize = resources.getBoolean(R.bool.isTablet)
        if(isTabletSize) {
            toolbar.layoutParams.height = resources.getDimension(R.dimen.action_bar_size).toInt()
        }

        hamburgerMenuIcon.setOnClickListener {
            val popup = PopupMenu(this, hamburgerMenuIcon)
            inflateDropdownMenu(popup, isTabletSize)
            popup.setOnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.action_work_ticker -> {
                        val lastTicket = viewModel.tickets.value?.lastOrNull()
                        if(lastTicket == null) {
                            val snackbar = Snackbar.make(this.findViewById(android.R.id.content),
                                getString(R.string.add_ticket_first), Snackbar.LENGTH_LONG)
                            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 2
                            snackbar.show()
                        } else {
                            startActivity(Intent(applicationContext, WorkTicketActivity::class.java). apply {
                                putExtra(EXTRA_TICKET, lastTicket)
                            })
                        }
                    }
                    R.id.action_get_directions -> {
                        startActivity(Intent(applicationContext, GetDirectionsActivity::class.java))
                    }
                }
                true
            }
            popup.show()
        }

        newTicketBarButton.setOnClickListener {
            startActivity(Intent(applicationContext, NewTicketActivity::class.java))
        }

        val adapter = TicketsAdapter(TicketsAdapter.CallListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))
            startActivity(callIntent)
        }, TicketsAdapter.ViewDetailsListener {
            startActivity(Intent(applicationContext, WorkTicketActivity::class.java). apply {
                putExtra(EXTRA_TICKET, it)
            })
        })
        binding.recyclerView.adapter = adapter

        viewModel.tickets.observe(this, {
            if(it.isNotEmpty()) {
                binding.noResultsAnimation.visibility = View.GONE
                binding.noResultsTextView.visibility = View.GONE
                binding.noResultsExplanationTextView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(it)
            } else {
                binding.noResultsAnimation.visibility = View.VISIBLE
                binding.noResultsTextView.visibility = View.VISIBLE
                binding.noResultsExplanationTextView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        })

    }

    /**
     * It syncs the due tickets to the device's calendars
     */
    private fun syncToCalendar() {
        if(viewModel.dueTickets.value?.isNotEmpty() == true) {
            val dueTickets = viewModel.dueTickets.value!!
            try {
                for (dueTicket in dueTickets) {
                    val timeInMilli = viewModel.toTimeInMilli(dueTicket.time)
                    addEventToCalendar(
                        applicationContext,
                        getString(R.string.event_title_template, dueTicket.clientName),
                        getString(R.string.address_template, dueTicket.address),
                        timeInMilli,
                        timeInMilli,
                        dueTicket.id.toString()
                    )
                }
                MaterialDialog(this)
                    .title(text = getString(R.string.sync_successful))
                    .message(text = getString(R.string.sync_successful_message))
                    .positiveButton(R.string.accept) {}
                    .show()
            } catch (e: Exception) {
                MaterialDialog(this)
                    .title(text = getString(R.string.sync_error))
                    .message(text = getString(R.string.sync_error_message))
                    .positiveButton(R.string.accept) {}
                    .show()
            }
        } else {
            MaterialDialog(this)
                .title(text = getString(R.string.not_synced))
                .message(text = getString(R.string.no_due_tickets))
                .positiveButton(R.string.accept) {}
                .show()
        }
    }

    /**
     * Requests permission to read and write the calendar
     */
    private fun requestCalendarPermission() {
        calendarPermissionWasRequested = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR), WRITE_TO_CALENDAR_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_TO_CALENDAR_PERMISSION_CODE) {
            if(calendarPermissionWasRequested) {
                calendarPermissionWasRequested = false
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    syncToCalendar()
                } else {
                    val snackbar = Snackbar.make(this.findViewById(android.R.id.content),
                        getString(R.string.calendar_permission_denied_explanation), Snackbar.LENGTH_LONG)
                        .setAction(R.string.allow) {
                            requestCalendarPermission()
                        }
                        .setActionTextColor(ContextCompat.getColor(applicationContext, R.color.snackbar_action))
                    snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 4
                    snackbar.show()
                }
            }
        }
    }





}