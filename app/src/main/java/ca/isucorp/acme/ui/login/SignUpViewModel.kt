package ca.isucorp.acme.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.database.model.User
import ca.isucorp.acme.repository.UserRepository
import ca.isucorp.acme.ui.DbAccessViewModel


class SignUpViewModel(application: Application) : DbAccessViewModel(application) {


    private val userRepository = UserRepository(database)

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user






    /**
     * Factory for constructing this viewmodel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
