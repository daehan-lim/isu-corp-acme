package ca.isucorp.acme.model

import java.time.LocalDateTime
import ca.isucorp.acme.database.model.Ticket

/**
 * A [Ticket] whose date has passed
 */
data class DueTicket (
    var clientName: String,
    var address: String,
    var time: LocalDateTime,
    var id: Long
)