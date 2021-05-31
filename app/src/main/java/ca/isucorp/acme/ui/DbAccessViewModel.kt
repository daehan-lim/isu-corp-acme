package ca.isucorp.acme.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.isucorp.acme.database.AcmeDatabase
import kotlinx.coroutines.*

/**
 * Base class for view models that use coroutines and access the app's database
 */
open class DbAccessViewModel(application: Application) : AndroidViewModel(application) {

    protected open val database = AcmeDatabase.getDatabase(application)

    @Suppress("PropertyName")
    protected open val _isError = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    open val isError: LiveData<Boolean>
        get() = _isError

    private val viewModelJob = Job()
    open val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    open fun handledError() {
        _isError.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}