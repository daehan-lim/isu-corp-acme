package ca.isucorp.acme.ui.editeticket

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import ca.isucorp.acme.ui.newticket.NewTicketViewModel

class EditTicketViewModel(application: Application) : NewTicketViewModel(application) {

    fun editTicket() {

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
