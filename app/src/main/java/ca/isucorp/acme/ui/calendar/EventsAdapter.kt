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

    private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")

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
            binding.itemFlightDateText.apply {
                val currentMonth = YearMonth.now()
                val currentMonth17 = currentMonth.atDay(17)
                currentMonth17.atTime(14, 0)
                text = formatter.format(currentMonth17.atTime(14, 0))
                setBackgroundColor(itemView.context.getColorCompat(R.color.black))
            }

            binding.itemDepartureAirportCodeText.text = dueTicket.clientName
            binding.itemDepartureAirportCityText.text = dueTicket.address

            binding.itemDestinationAirportCodeText.text = dueTicket.clientName
            binding.itemDestinationAirportCityText.text = dueTicket.address
        }
    }
}