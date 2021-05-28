package ca.isucorp.acme.ui.newticket

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.database.model.User
import ca.isucorp.acme.repository.UserRepository
import ca.isucorp.acme.ui.DbAccessViewModel
import ca.isucorp.acme.util.DAY_SHORT_MONTH_YEAR_PATTERN
import ca.isucorp.acme.util.TIME_PATTERN
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewTicketViewModel(application: Application) : DbAccessViewModel(application) {

    private val _dateText = MutableLiveData<String>()
    val dateText: LiveData<String> = _dateText

    private var dateString: String? = null

    private val userRepository = UserRepository(database)

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    /*private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState*/

    private val _isSigningSuccessful = MutableLiveData<Boolean>()
    val isSigningSuccessful: LiveData<Boolean> = _isSigningSuccessful


    fun setDate(timeInMillis: Long) {
        val date = getDateInCurrentTimeZone(timeInMillis)
        val dateStringParser = SimpleDateFormat(DAY_SHORT_MONTH_YEAR_PATTERN, Locale.ENGLISH)
        dateString = dateStringParser.format(date)
        _dateText.value = "$dateString, 12:00 PM"
    }

    /**
     * Get Date object given dateInMillis, regardless of daylight savings time
     * @param dateInMillis The date in milliseconds
     */
    private fun getDateInCurrentTimeZone(dateInMillis: Long): Date {
        // Get the offset from our timezone and UTC.
        val timeZoneUTC = TimeZone.getDefault()
        val calendar = Calendar.getInstance()
        // It will be negative, so that's the -1
        val offsetFromUTC = timeZoneUTC.getOffset(calendar.timeInMillis) * -1
        return Date(dateInMillis + offsetFromUTC)
    }

    fun setTime(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        val dateStringParser = SimpleDateFormat(TIME_PATTERN, Locale.ENGLISH)
        _dateText.value = "$dateString, ${dateStringParser.format(calendar.time)}"
    }

/*fun loginDataChanged(username: String, password: String) {
    val usernameErrorId = usernameError(username)
    if (usernameErrorId != null) {
        _loginFormState.value = LoginFormState(usernameError = usernameErrorId)
        return
    }
    val passwordErrorId = passwordError(password)
    if(passwordErrorId != null) {
        _loginFormState.value = LoginFormState(passwordError = passwordErrorId)
        return
    }
    _loginFormState.value = LoginFormState(isDataValid = true)
}

fun signupDataChanged(username: String, password: String, secondPassword: String) {
    val usernameErrorId = usernameError(username)
    if (usernameErrorId != null) {
        _loginFormState.value = LoginFormState(usernameError = usernameErrorId)
        return
    }
    val passwordErrorId = passwordError(password)
    if(passwordErrorId != null) {
        _loginFormState.value = LoginFormState(passwordError = passwordErrorId)
        return
    }
    val secondPasswordErrorId = secondPasswordError(password, secondPassword)
    if(secondPasswordErrorId != null) {
        _loginFormState.value = LoginFormState(secondPasswordError = secondPasswordErrorId)
        return
    }
    _loginFormState.value = LoginFormState(isDataValid = true)
}*/

    /**
     * Returns the id of the string resource containing the error in the username field or null if the field is valid
     * @param username The username input by the user.
     */
    private fun usernameError(username: String): Int? {
        if(username.isEmpty()) {
            return R.string.username_empty_error
        }
        if(username.length < 4) {
            return R.string.username_short_error
        }
        return null
    }

    /**
     * Returns the id of the string resource containing the error in the password field or null if the field is valid
     * @param password The password input by the user.
     */
    private fun passwordError(password: String): Int? {
        if(password.isEmpty()) {
            return R.string.password_empty_error
        }
        if(password.length < 8) {
            return R.string.password_short_error
        }
        return null
    }

    private fun secondPasswordError(password: String, secondPassword: String): Int? {
        passwordError(secondPassword).apply {
            if(this != null) {
                return this
            }
        }
        if(password != secondPassword) {
            return R.string.password_not_match
        }
        return null
    }

    fun register(username: String, password: String) {
        try {
            coroutineScope.launch {
                userRepository.registerUser(username, password)
                val registeredUser = userRepository.findUser(username, password)
                _isSigningSuccessful.value = registeredUser != null
            }
        } catch (e: Exception) {
            _isSigningSuccessful.value = false
        }
    }


    fun login(username: String, password: String) {
        try {
            coroutineScope.launch {
                val registeredUser = userRepository.findUser(username, password)
                _isSigningSuccessful.value = registeredUser != null
            }
        } catch (e: Exception) {
            _isSigningSuccessful.value = false
        }
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
