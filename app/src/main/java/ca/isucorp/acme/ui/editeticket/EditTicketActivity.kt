package ca.isucorp.acme.ui.editeticket

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.BR
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


        binding.layoutSimpleAppBar.toolbar.findViewById<TextView>(R.id.toolbar_title).text = getString(R.string.edit_ticket)

        val ticket = (intent?.getSerializableExtra(EXTRA_TICKET) ?: finish()) as Ticket
        clientNameEditText.setText(ticket.clientName)
        addressEditText.setText(ticket.address)

        binding.layoutSimpleAppBar.toolbar.findViewById<ImageView>(R.id.button_delete).apply {
            visibility = View.VISIBLE
            setOnClickListener {
                MaterialDialog(this@EditTicketActivity)
                    .title(text = getString(R.string.remove_ticker))
                    .message(text = getString(R.string.remove_ticker_message))
                    .positiveButton(R.string.accept) {
                        viewModel.removeTicket(ticket.id!!)
                    }
                    .negativeButton(R.string.cancel)
                    .show()
            }
        }

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

        viewModel.manageTicketFormState.observe(this, Observer {
            val formState = it ?: return@Observer

            if (formState.isTicketEdited || formState.isTicketRemoved) {
                var title = R.string.ticket_edited
                var message = R.string.ticket_edited_message
                if(formState.isTicketRemoved) {
                    title = R.string.ticket_removed
                    message = R.string.ticket_removed_message
                }
                val materialDialog = MaterialDialog(this)
                    .title(text = getString(title))
                    .message(text = getString(message))
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