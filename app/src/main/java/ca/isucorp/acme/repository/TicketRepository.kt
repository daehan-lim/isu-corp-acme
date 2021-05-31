package ca.isucorp.acme.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ca.isucorp.acme.database.AcmeDatabase
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.model.DueTicket
import ca.isucorp.acme.util.SHORT_DATE_AND_TIME_PATTERN
import ca.isucorp.acme.util.convertToLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * It provides an abstraction of the [Ticket] table of the database
 * with methods and attributes to manage tickets without the calling class having to know
 * about the database
 */
class TicketRepository(private val database: AcmeDatabase) {

    /**
     * Contains a reference to all the tickets in the database through a [LiveData] that
     * gets updated every time the ticket table changes
     */
    val tickets = database.ticketDao.getAllTickets()

    /**
     * Contains a reference to all the due tickets in the database through a [LiveData] that
     * gets updated every time the ticket table changes
     */
    val dueTickets: LiveData<List<DueTicket>> = Transformations.map(tickets) { tickets ->
        val dueTickets = mutableListOf<DueTicket>()
        for(ticket in tickets) {
            val dateStringParser = SimpleDateFormat(SHORT_DATE_AND_TIME_PATTERN, Locale.ENGLISH)
            val ticketDate = dateStringParser.parse(ticket.date)
            val nowDate = Date()
            if(!ticketDate!!.before(nowDate)) {
                dueTickets.add(DueTicket(ticket.clientName, ticket.address, convertToLocalDateTime(ticketDate), ticket.id!!))
            }
        }
        dueTickets
    }

    /**
     * Adds a new ticket to the app
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher.
     *
     */
    suspend fun addTicket(clientName: String, address: String, date: String, phone: String, notes: String, reasonsForCall: String): Long {
        return withContext(Dispatchers.IO) {
            database.ticketDao.insert(Ticket(clientName, address, date, phone, notes, reasonsForCall))
        }
    }

    /**
     * Modifies a [Ticket]'s fields
     */
    suspend fun updateTicket(ticket: Ticket) {
        withContext(Dispatchers.IO) {
            database.ticketDao.insert(ticket)
        }
    }

    /**
     * It returns the id of a ticket or null if it is not found in the database
     */
    suspend fun findTicket(id: Long): Ticket? {
        return withContext(Dispatchers.IO) {
            database.ticketDao.findTicket(id)
        }
    }

    /**
     * It removes a ticket given its id
     */
    suspend fun removeTicket(id: Long) {
        withContext(Dispatchers.IO) {
            database.ticketDao.remove(id)
        }
    }

}
