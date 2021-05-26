package ca.isucorp.acme.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.isucorp.acme.database.AcmeDatabase
import kotlinx.coroutines.*

open class DbAccessViewModel(application: Application) : AndroidViewModel(application) {

    protected open val database = AcmeDatabase.getDatabase(application)

    protected open val _error = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    open val error: LiveData<Boolean>
        get() = _error

    val viewModelJob = Job()
    open val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    open fun handledError() {
        _error.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}