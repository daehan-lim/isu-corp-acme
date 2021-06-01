package ca.isucorp.acme.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ca.isucorp.acme.database.model.User

/**
 * Room's Database Access Object class for the [User] table
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table WHERE id = :id")
    suspend fun remove(id: Long)

    @Query("DELETE FROM user_table")
    suspend fun clearTable()

    @Query("SELECT * FROM user_table WHERE user_name = :userName AND password = :password")
    fun findUser(userName: String, password: String ): User?


}
