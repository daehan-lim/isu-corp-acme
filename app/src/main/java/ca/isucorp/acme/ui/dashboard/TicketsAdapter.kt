package ca.isucorp.acme.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.databinding.ListItemTicketBinding

class TicketsAdapter(private val callListener: CallListener,
                     private val viewDetailsListener: ViewDetailsListener) : RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    var data = listOf<Ticket?>()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = data[position]
        viewHolder.bind(item, callListener, viewDetailsListener)
    }

    open class ViewHolder internal constructor(open val binding: ListItemTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ticket?, callListener: CallListener, viewDetailsListener: ViewDetailsListener) {
            binding.ticket = item
            binding.viewDetailsListener = viewDetailsListener
            binding.callListener = callListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ListItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }

    }


    class CallListener(val callButtonListener: (phone: String) -> Unit) {
        fun onClick(ticket: Ticket) = callButtonListener(ticket.phone ?: "")
    }

    class ViewDetailsListener(val viewDetailsListener: (ticket: Ticket) -> Unit) {
        fun onClick(ticket: Ticket) = viewDetailsListener(ticket)
    }


}