package ca.isucorp.acme.repository

import ca.isucorp.acme.database.AcmeDatabase
import ca.isucorp.acme.database.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class to manage [User] objects using the Repository pattern with the MVVM architecture
 */
class UserRepository(private val database: AcmeDatabase) {


    /**
     * Register a new user in the app
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher.
     *
     */
    suspend fun registerUser(username: String, password: String) {
        withContext(Dispatchers.IO) {
            database.userDao.insert(User(username, password))
        }
    }

    /**
     * It returns a user id given their user name and password, or null if the user does not exist
     */
    suspend fun findUser(username: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            database.userDao.findUser(username, password)
        }
    }
}
