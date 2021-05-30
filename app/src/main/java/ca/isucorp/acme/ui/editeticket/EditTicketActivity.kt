package ca.isucorp.acme.ui.editeticket

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.database.model.Ticket
import ca.isucorp.acme.ui.dashboard.EXTRA_TICKET
import ca.isucorp.acme.ui.newticket.NewTicketActivity
import ca.isucorp.acme.util.*
import com.afollestad.materialdialogs.MaterialDialog


class EditTicketActivity : NewTicketActivity() {

    override val viewModel: EditTicketViewModel by lazy {
        ViewModelProvider(this, EditTicketViewModel.Factory(application)).get(EditTicketViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.layoutSimpleAppBar.toolbar.findViewById<ImageView>(R.id.button_delete).apply {
            visibility = View.VISIBLE
        }

        binding.layoutSimpleAppBar.toolbar.findViewById<TextView>(R.id.toolbar_title).text = getString(R.string.edit_ticket)

        val ticket = intent?.getSerializableExtra(EXTRA_TICKET) as Ticket
        clientNameEditText.setText(ticket.clientName)
        addressEditText.setText(ticket.address)

        viewModel.setDate(ticket.date)

        phoneEditText.setText(ticket.phone)
        notesEditText.setText(ticket.notes)
        reasonsForCallEditText.setText(ticket.reasonsForCall)

        saveButton.setOnClickListener {
            viewModel.editTicket(
                clientNameEditText.text.toString(),
                addressEditText.text.toString(),
                phoneEditText.text.toString(),
                notesEditText.text.toString(),
                reasonsForCallEditText.text.toString(),
                ticket.id!!
            )
        }

        viewModel.newTicketFormState.observe(this, Observer {
            val formState = it ?: return@Observer

            if (formState.isTicketEdited) {
                val materialDialog = MaterialDialog(this)
                    .title(text = getString(R.string.ticket_edited))
                    .message(text = getString(R.string.ticket_edited_message))
                    .positiveButton(R.string.accept) {
                        finish()
                        goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
                    }
                materialDialog.setOnCancelListener {
                    finish()
                    goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
                }
                materialDialog.show()
                return@Observer
            }

            updateFieldsWithErrorMessage(formState)
        })

    }



}