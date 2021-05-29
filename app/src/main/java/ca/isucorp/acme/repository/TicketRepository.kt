package ca.isucorp.acme.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ca.isucorp.acme.database.AcmeDatabase
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.database.model.User
import ca.isucorp.acme.model.DueTicket
import ca.isucorp.acme.util.convertToLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class TicketRepository(private val database: AcmeDatabase) {


    val tickets = database.ticketDao.getAllTickets()

    val dueTickets: LiveData<List<DueTicket>> = Transformations.map(tickets) { ticket ->
        ticket.map {
            DueTicket(it.clientName, it.address, time = convertToLocalDateTime(it.date))
        }
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
