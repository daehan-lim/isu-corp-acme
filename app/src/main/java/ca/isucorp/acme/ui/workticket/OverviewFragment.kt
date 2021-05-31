package ca.isucorp.acme.ui.workticket

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.isucorp.acme.R
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.databinding.FragmentOverviewBinding
import ca.isucorp.acme.ui.dashboard.EXTRA_TICKET
import ca.isucorp.acme.ui.directions.GetDirectionsActivity
import ca.isucorp.acme.util.FULL_DATE_AND_TIME_PATTERN
import ca.isucorp.acme.util.SHORT_DATE_AND_TIME_PATTERN
import ca.isucorp.acme.util.makeScrollableInsideScrollView
import java.text.SimpleDateFormat
import java.util.*

const val EXTRA_ADDRESS = "ca.isucorp.acme.ui.workticket.OverviewFragment.ADDRESS"
class OverviewFragment:  Fragment() {
    private lateinit var binding: FragmentOverviewBinding

    private var isTabletSize: Boolean = false
    private var isLandscape: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater)

        isTabletSize = resources.getBoolean(R.bool.isTablet)
        isLandscape = resources.getBoolean(R.bool.isLandscape)

        val ticket = activity?.intent?.getSerializableExtra(EXTRA_TICKET) as Ticket

        binding.textClient.text = ticket.clientName
        binding.textPhone.apply {
            visibility = when (ticket.phone.isNullOrEmpty()) {
                true -> View.GONE
                else -> {
                    text = ticket.phone
                    val onClickListener = View.OnClickListener {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${ticket.phone}"))
                        startActivity(callIntent)
                    }
                    binding.imagePhone.setOnClickListener(onClickListener)
                    this.setOnClickListener(onClickListener)
                    View.VISIBLE
                }
            }
        }
        binding.imagePhone.visibility = binding.textPhone.visibility

        var dateStringParser = SimpleDateFormat(SHORT_DATE_AND_TIME_PATTERN, Locale.ENGLISH)
        val date = dateStringParser.parse(ticket.date)
        dateStringParser = SimpleDateFormat(FULL_DATE_AND_TIME_PATTERN, Locale.ENGLISH)
        binding.textScheduledFor.text = dateStringParser.format(date!!)

        binding.textAddress.text = ticket.address
        val getDirectionsListener = View.OnClickListener {
            startActivity(Intent(requireContext(), GetDirectionsActivity::class.java).apply {
                putExtra(EXTRA_ADDRESS, ticket.address)
            })
        }
        binding.buttonGetDirections.setOnClickListener(getDirectionsListener)
        binding.imageAddress.setOnClickListener(getDirectionsListener)
        binding.textAddress.setOnClickListener(getDirectionsListener)
        binding.textHeadingAddress.setOnClickListener(getDirectionsListener)

        binding.textNotes.text = ticket.notes

        binding.textReasonForCall.text = ticket.reasonsForCall

        binding.textTicketId.text = getString(R.string.ticket_id_formatted, ticket.id)

        if(isLandscape || isTabletSize) {
            if (binding.textNotes.text.length >= 25) {
                binding.textNotes.movementMethod = ScrollingMovementMethod()
                binding.textNotes.makeScrollableInsideScrollView()
            }
        } else {
            binding.imageGetDirections?.setOnClickListener(getDirectionsListener)
        }
        return binding.root
    }
}
