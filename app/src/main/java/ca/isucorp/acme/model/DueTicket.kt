package ca.isucorp.acme.model

import java.time.LocalDateTime
import java.util.*



data class DueTicket (
    var clientName: String,
    var address: String,
    var time: LocalDateTime,
)