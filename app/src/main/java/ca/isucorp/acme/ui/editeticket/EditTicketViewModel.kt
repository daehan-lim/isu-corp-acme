package ca.isucorp.acme.ui.editeticket

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.ui.newticket.ManageTicketFormState

import ca.isucorp.acme.ui.newticket.NewTicketViewModel
import kotlinx.coroutines.launch

class EditTicketViewModel(application: Application) : NewTicketViewModel(application) {

    fun editTicket(clientName: String, address: String, phone: String, notes: String, reasonsForCall: String, id: Long) {
        if (!isFormValid(clientName, address, phone)) {
            return
        }
        ticket = Ticket(clientName, address, _dateText.value ?: "", phone, notes, reasonsForCall, id)
        coroutineScope.launch {
            try {
                ticketRepository.updateTicket(ticket!!)
                _manageTicketFormState.value = ManageTicketFormState(isTicketEdited = true)
            } catch (e: Exception) {
                _manageTicketFormState.value = ManageTicketFormState(isTicketEdited = false)
            }
        }
    }

    fun removeTicket(id: Long) {
        coroutineScope.launch {
            try {
                ticketRepository.removeTicket(id)
                _manageTicketFormState.value = ManageTicketFormState(isTicketRemoved = true)
            } catch (e: Exception) {
                _manageTicketFormState.value = ManageTicketFormState(isTicketRemoved = false)
            }
        }
    }


    /**
     * Factory for constructing this viewmodel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditTicketViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditTicketViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
