package ca.isucorp.acme.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.database.model.User


@Dao
interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: Ticket)

    @Query("DELETE FROM ticket_table WHERE id = :id")
    suspend fun remove(id: Long)

    @Query("DELETE FROM ticket_table")
    suspend fun clearTable()

    @Query("select * from ticket_table")
    fun getAllTickets(): LiveData<List<Ticket>>


}
