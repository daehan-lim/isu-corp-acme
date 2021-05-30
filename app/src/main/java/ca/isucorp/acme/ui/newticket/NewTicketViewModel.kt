package ca.isucorp.acme.ui.newticket

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.repository.TicketRepository
import ca.isucorp.acme.ui.DbAccessViewModel
import ca.isucorp.acme.util.DAY_SHORT_MONTH_YEAR_PATTERN
import ca.isucorp.acme.util.TIME_PATTERN
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

open class NewTicketViewModel(application: Application) : DbAccessViewModel(application) {

    protected open val _dateText = MutableLiveData<String>()
    open val dateText: LiveData<String>
        get() = _dateText

    protected open var dateString: String? = null

    protected open val ticketRepository: TicketRepository
        get() = TicketRepository(database)

    protected open val _newTicketFormState = MutableLiveData<NewTicketFormState?>()
    open val newTicketFormState: LiveData<NewTicketFormState?>
        get() = _newTicketFormState


    open fun setDate(timeInMillis: Long) {
        val date = getDateInCurrentTimeZone(timeInMillis)
        val dateStringParser = SimpleDateFormat(DAY_SHORT_MONTH_YEAR_PATTERN, Locale.ENGLISH)
        dateString = dateStringParser.format(date)
        _dateText.value = "$dateString, 12:00 PM"
    }

    open fun setDate(date: String) {
        _dateText.value = date
    }

    /**
     * Get Date object given dateInMillis, regardless of daylight savings time
     * @param dateInMillis The date in milliseconds
     */
    protected open fun getDateInCurrentTimeZone(dateInMillis: Long): Date {
        // Get the offset from our timezone and UTC.
        val timeZoneUTC = TimeZone.getDefault()
        val calendar = Calendar.getInstance()
        // It will be negative, so that's the -1
        val offsetFromUTC = timeZoneUTC.getOffset(calendar.timeInMillis) * -1
        return Date(dateInMillis + offsetFromUTC)
    }

    open fun setTime(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        val dateStringParser = SimpleDateFormat(TIME_PATTERN, Locale.ENGLISH)
        _dateText.value = "$dateString, ${dateStringParser.format(calendar.time)}"
    }

    /**
     * Returns the current system hour in a 24-hour clock
     */
    open fun getCurrentHour(): Int {
        return Calendar.getInstance()[Calendar.HOUR_OF_DAY]
    }

    /**
     * Returns the current system minute
     */
    open fun getCurrentMinute(): Int {
        return Calendar.getInstance()[Calendar.MINUTE]
    }


    fun addTicket(clientName: String, address: String, phone: String, notes: String, reasonsForCall: String) {
        if (!isFormValid(clientName, address, phone)) {
            return
        }
        coroutineScope.launch {
            try {
                val id = ticketRepository.addTicket(clientName, address, _dateText.value ?: "", phone, notes, reasonsForCall)
                val addedTicket = ticketRepository.findTicket(id)
                if(addedTicket != null) {
                    _newTicketFormState.value = NewTicketFormState(isTicketAdded = true)
                } else {
                    _newTicketFormState.value = NewTicketFormState(isTicketAdded = false)
                }
            } catch (e: Exception) {
                _newTicketFormState.value = NewTicketFormState(isTicketAdded = false)
            }
        }
    }

    protected open fun isFormValid(clientName: String, address: String, phone: String): Boolean {
        val clientNameErrorId = clientNameError(clientName)
        if (clientNameErrorId != null) {
            _newTicketFormState.value = NewTicketFormState(clientNameError = clientNameErrorId)
            return false
        }
        val addressErrorId = addressError(address)
        if (addressErrorId != null) {
            _newTicketFormState.value = NewTicketFormState(addressError = addressErrorId)
            return false
        }
        val dateErrorId = dateError(_dateText.value ?: "")
        if (dateErrorId != null) {
            _newTicketFormState.value = NewTicketFormState(dateError = dateErrorId)
            return false
        }
        val phoneErrorId = phoneError(phone)
        if (phoneErrorId != null) {
            _newTicketFormState.value = NewTicketFormState(phoneError = phoneErrorId)
            return false
        }
        return true
    }

    /**
     * Returns the id of the string resource containing the error in the client name field or null if the field is valid
     * @param clientName The client name input by the user.
     */
    protected open fun clientNameError(clientName: String): Int? {
        if(clientName.isEmpty()) {
            return R.string.client_name_empty_error
        }
        if(clientName.length < 4) {
            return R.string.client_name_short_error
        }
        val pattern = Pattern.compile("[^A-Za-zÀ-ú ]")
        val matcher = pattern.matcher(clientName)
        if (matcher.find()) {
            return R.string.client_name_invalid
        }
        return null
    }

    /**
     * Returns the id of the string resource containing the error in the address field or null if the field is valid
     * @param date The address input by the user.
     */
    protected open fun dateError(date: String): Int? {
        if(date.isEmpty()) {
            return R.string.date_empty_error
        }
        return null
    }

    /**
     * Returns the id of the string resource containing the error in the address field or null if the field is valid
     * @param address The address input by the user.
     */
    protected open fun addressError(address: String): Int? {
        if(address.isEmpty()) {
            return R.string.address_empty_error
        }
        if(address.length < 4) {
            return R.string.address_short_error
        }
        return null
    }

    /**
     * Returns the id of the string resource containing the error in the phone field or null if the field is valid
     * @param phone The phone input by the user.
     */
    protected open fun phoneError(phone: String): Int? {
        if(phone.isNotEmpty() && !Patterns.PHONE.matcher(phone).matches()) {
            return R.string.wrong_phone
        }
        return null
    }


    fun handledNewTicket() {
        _newTicketFormState.value = null
    }



    /**
     * Factory for constructing this viewmodel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewTicketViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewTicketViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
