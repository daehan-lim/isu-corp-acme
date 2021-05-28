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

    private var date: String? = null

    private val userRepository = UserRepository(database)

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    private val calendar = Calendar.getInstance()

    /*private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState*/

    private val _isSigningSuccessful = MutableLiveData<Boolean>()
    val isSigningSuccessful: LiveData<Boolean> = _isSigningSuccessful


    fun setDate(timeInMillis: Long) {
        calendar.timeInMillis = timeInMillis
        val dateStringParser = SimpleDateFormat(DAY_SHORT_MONTH_YEAR_PATTERN, Locale.ENGLISH)
        date = dateStringParser.format(calendar.time)
        _dateText.value = "$date, 12:00 PM"
    }

    fun setTime(hour: Int, minute: Int) {
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        val dateStringParser = SimpleDateFormat(TIME_PATTERN, Locale.ENGLISH)
        _dateText.value = "$date, ${dateStringParser.format(calendar.time)}"
        /*
        var hourString = hour.toString()
        var timePeriod = "AM"
        if(hour < 10) {
            hourString = "0$hourString"
        } else if(hour >= 12) {
            timePeriod = "PM"
        }
        val minuteString = when(minute < 10) {
            true -> "0$minute"
            else -> minute.toString()
        }
        _dateText.value = "$date, $hourString:$minuteString $timePeriod"*/
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
