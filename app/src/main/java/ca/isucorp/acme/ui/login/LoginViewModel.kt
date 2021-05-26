package ca.isucorp.acme.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.database.model.User
import ca.isucorp.acme.repository.UserRepository
import ca.isucorp.acme.ui.DbAccessViewModel


class LoginViewModel(application: Application) : DbAccessViewModel(application) {

    private val userRepository = UserRepository(database)

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val _userNameError = MutableLiveData<String?>()
    val userNameError: LiveData<String?> = _userNameError


    fun loginDataChanged(username: String, password: String) {
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



    /**
     * Factory for constructing this viewmodel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
