package ca.isucorp.acme.database.model

import androidx.room.*
import java.util.*


@Entity(tableName = "ticket_table")
data class Ticket (
    @ColumnInfo(name = "client_name")
    var clientName: String,

    @ColumnInfo(name = "address")
    var address: String,

    @ColumnInfo(name = "date")
    var date: Date,

    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @ColumnInfo(name = "notes")
    var notes: String? = null,

    @ColumnInfo(name = "reasons_for_call")
    var reasonsForCall: String? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
)