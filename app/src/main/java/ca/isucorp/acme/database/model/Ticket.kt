package ca.isucorp.acme.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Class that represents a "ticket" table in the database
 */
@Entity(tableName = "ticket_table")
data class Ticket (
    @ColumnInfo(name = "client_name")
    var clientName: String,

    @ColumnInfo(name = "address")
    var address: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @ColumnInfo(name = "notes")
    var notes: String? = null,

    @ColumnInfo(name = "reasons_for_call")
    var reasonsForCall: String? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
): Serializable