package ca.isucorp.acme.ui.calendar

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.isucorp.acme.R
import ca.isucorp.acme.model.DueTicket
import ca.isucorp.acme.databinding.ListItemEventBinding
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    val tickets = mutableListOf<DueTicket>()

    private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            ListItemEventBinding.inflate(parent.context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: EventsViewHolder, position: Int) {
        viewHolder.bind(tickets[position])
    }

    override fun getItemCount(): Int = tickets.size

    inner class EventsViewHolder(val binding: ListItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dueTicket: DueTicket) {
            binding.dueDate.apply {
                text = formatter.format(dueTicket.time)
            }

            binding.dueTicketClient.text = dueTicket.clientName
            binding.dueTicketAddress.text = dueTicket.address
        }
    }
}