package ca.isucorp.acme.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginSignUpFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val secondPasswordError: Int? = null,
    val isDataValid: Boolean = false
)