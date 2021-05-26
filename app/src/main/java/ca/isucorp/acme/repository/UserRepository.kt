package ca.isucorp.acme.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ca.isucorp.acme.database.AcmeDatabase
import ca.isucorp.acme.database.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val database: AcmeDatabase) {


    /**
     * Register a new user in the app
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher.
     *
     */
    suspend fun registerUser(user: User) {
        withContext(Dispatchers.IO) {
            database.userDao.insert(user)
        }
    }

    suspend fun findUser(username: String, password: String): LiveData<User?> {
        return withContext(Dispatchers.IO) {
            database.userDao.findUser(username, password)
        }
    }
}
