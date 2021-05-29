package ca.isucorp.acme.ui.calendar

import android.app.Application
import androidx.lifecycle.*
import ca.isucorp.acme.model.DueTicket
import ca.isucorp.acme.repository.TicketRepository
import ca.isucorp.acme.ui.DbAccessViewModel
import java.time.LocalDate

class CalendarViewModel(application: Application) : DbAccessViewModel(application) {

    private val ticketRepository = TicketRepository(database)

    val dueTickets: LiveData<Map<LocalDate, List<DueTicket>>> = Transformations.map(ticketRepository.dueTickets) { dueTickets ->
        dueTickets.groupBy { it.time.toLocalDate() }
    }
    /**
     * Factory for constructing this viewmodel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CalendarViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
