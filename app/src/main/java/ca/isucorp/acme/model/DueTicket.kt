package ca.isucorp.acme.model

import java.time.LocalDateTime
import java.util.*



data class DueTicket (
    var clientName: String,

    var address: String,

    var date: String,

    var phone: String? = null,

    var notes: String? = null,

    var reasonsForCall: String? = null,

    var time: LocalDateTime,

    var id: Long? = null,
)