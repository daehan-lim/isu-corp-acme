package ca.isucorp.acme.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ca.isucorp.acme.database.AcmeDatabase
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.database.model.User
import ca.isucorp.acme.model.DueTicket
import ca.isucorp.acme.util.DATE_AND_TIME_PATTERN
import ca.isucorp.acme.util.convertToLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class TicketRepository(private val database: AcmeDatabase) {


    val tickets = database.ticketDao.getAllTickets()

    val dueTickets: LiveData<List<DueTicket>> = Transformations.map(tickets) { tickets ->
        val dueTickets = mutableListOf<DueTicket>()
        for(ticket in tickets) {
            val dateStringParser = SimpleDateFormat(DATE_AND_TIME_PATTERN, Locale.ENGLISH)
            val ticketDate = dateStringParser.parse(ticket.date)
            val nowDate = Date()
            if(!ticketDate!!.before(nowDate)) {
                dueTickets.add(DueTicket(ticket.clientName, ticket.address, convertToLocalDateTime(ticketDate), ticket.id!!))
            }
        }
        dueTickets
    }

    /**
     * Register a new ticket in the app
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher.
     *
     */
    suspend fun addTicket(clientName: String, address: String, date: String, phone: String, notes: String, reasonsForCall: String): Long {
        return withContext(Dispatchers.IO) {
            database.ticketDao.insert(Ticket(clientName, address, date, phone, notes, reasonsForCall))
        }
    }

    suspend fun findTicket(id: Long): Ticket? {
        return withContext(Dispatchers.IO) {
            database.ticketDao.findTicket(id)
        }
    }

}
