package ca.isucorp.acme.ui.newticket

/**
 * Data validation state of the new ticket form.
 */
data class NewTicketFormState(
    val clientNameError: Int? = null,
    val addressError: Int? = null,
    val dateError: Int? = null,
    val phoneError: Int? = null,
    val isTicketAdded: Boolean = false,
    val isTicketEdited: Boolean = false
)