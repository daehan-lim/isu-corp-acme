package ca.isucorp.acme.repository

import androidx.lifecycle.LiveData
import ca.isucorp.acme.database.AcmeDatabase
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.database.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketRepository(private val database: AcmeDatabase) {


    val tickets = database.ticketDao.getAllTickets()

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
