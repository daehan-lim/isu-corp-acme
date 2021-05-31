package ca.isucorp.acme.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.databinding.ListItemTicketBinding

/**
 * Adapter used to manage a list of tickets
 */
class TicketsAdapter(private val callListener: CallListener, private val viewDetailsListener: ViewDetailsListener)
    : ListAdapter<Ticket, TicketsAdapter.ViewHolder>(TicketDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
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

    /**
     * Class used to communicate the activity with the layout when the phone field gets clicked
     */
    class CallListener(val callButtonListener: (phone: String) -> Unit) {
        fun onClick(ticket: Ticket) = callButtonListener(ticket.phone ?: "")
    }

    /**
     * Class used to communicate the activity with the layout when the list item gets clicked
     */
    class ViewDetailsListener(val viewDetailsListener: (ticket: Ticket) -> Unit) {
        fun onClick(ticket: Ticket) = viewDetailsListener(ticket)
    }

    /**
     * This class calculates the difference between two lists and outputs a
     * list of update operations that converts the first list into the second one.
     */
    class TicketDiffCallback: DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return  oldItem == newItem
        }

    }

}