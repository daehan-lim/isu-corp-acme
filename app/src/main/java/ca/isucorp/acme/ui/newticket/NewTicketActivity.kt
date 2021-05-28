package ca.isucorp.acme.ui.newticket

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityNewTicketBinding
import ca.isucorp.acme.util.*
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class NewTicketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewTicketBinding
    private lateinit var selectDateTextInput: TextInputLayout
    private lateinit var selectDateEditText: MaterialAutoCompleteTextView
    private lateinit var saveButton: MaterialButton
    private lateinit var clientNameTextInput: TextInputLayout
    private lateinit var clientNameEditText: TextInputEditText
    private lateinit var addressTextInput: TextInputLayout
    private lateinit var addressEditText: TextInputEditText
    private lateinit var phoneTextInput: TextInputLayout
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var notesTextInput: TextInputLayout
    private lateinit var notesEditText: TextInputEditText
    private lateinit var reasonsForCallTextInput: TextInputLayout
    private lateinit var reasonsForCallEditText: TextInputEditText
    private val viewModel: NewTicketViewModel by lazy {
        ViewModelProvider(this, NewTicketViewModel.Factory(application)).get(NewTicketViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectDateTextInput = binding.layoutActivityNewTicketContent.findViewById(R.id.text_input_select_date)
        selectDateEditText = binding.layoutActivityNewTicketContent.findViewById(R.id.edit_select_date)
        saveButton = binding.layoutActivityNewTicketContent.findViewById(R.id.button_save)
        clientNameTextInput = binding.layoutActivityNewTicketContent.findViewById(R.id.text_input_client_name)
        clientNameEditText = binding.layoutActivityNewTicketContent.findViewById(R.id.edit_client_name)
        addressTextInput = binding.layoutActivityNewTicketContent.findViewById(R.id.text_input_address)
        addressEditText = binding.layoutActivityNewTicketContent.findViewById(R.id.edit_address)
        phoneTextInput = binding.layoutActivityNewTicketContent.findViewById(R.id.text_input_phone)
        phoneEditText = binding.layoutActivityNewTicketContent.findViewById(R.id.edit_phone)
        notesTextInput = binding.layoutActivityNewTicketContent.findViewById(R.id.text_input_notes)
        notesEditText = binding.layoutActivityNewTicketContent.findViewById(R.id.edit_notes)
        reasonsForCallTextInput = binding.layoutActivityNewTicketContent.findViewById(R.id.text_input_reasons)
        reasonsForCallEditText = binding.layoutActivityNewTicketContent.findViewById(R.id.edit_reasons)

        val toolbar = binding.layoutSimpleAppBar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val toolBarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.new_ticket)
        toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)

        selectDateEditText.setOnClickListener {
            showDatePicker()
        }

        selectDateTextInput.setEndIconOnClickListener {
            showDatePicker()
        }

        viewModel.dateText.observe(this, {
            selectDateEditText.setText(it)
        })

        saveButton.setOnClickListener {
            viewModel.addTicket(
                clientNameEditText.text.toString(),
                addressEditText.text.toString(),
                phoneEditText.text.toString(),
                notesEditText.text.toString(),
                reasonsForCallEditText.text.toString(),
            )
        }

        viewModel.newTicketFormState.observe(this, Observer {
            val formState = it ?: return@Observer

            if(formState.isTicketAdded) {
                val materialDialog = MaterialDialog(this)
                    .title(text = getString(R.string.ticket_added))
                    .message(text = getString(R.string.ticket_added_message))
                    .positiveButton(R.string.accept) {
                        resetForm()
                    }
                materialDialog.setOnCancelListener {
                    resetForm()
                }
                materialDialog.show()
                return@Observer
            }

            binding.scrollView.smoothScrollTo(0, 0)

            clientNameTextInput.error = when(formState.clientNameError) {
                null -> null
                else -> getString(formState.clientNameError)
            }
            addressTextInput.error = when(formState.addressError) {
                null -> null
                else -> getString(formState.addressError)
            }
            selectDateTextInput.error = when(formState.dateError) {
                null -> null
                else -> getString(formState.dateError)
            }
            phoneTextInput.error = when(formState.phoneError) {
                null -> null
                else -> getString(formState.phoneError)
            }
        })

    }

    private fun resetForm() {
        viewModel.handledNewTicket()

        clientNameEditText.setText("")
        addressEditText.setText("")
        selectDateEditText.setText(getString(R.string.select_date_input))
        phoneEditText.setText("")
        notesEditText.setText("")
        clientNameTextInput.error = null
        addressTextInput.error = null
        selectDateTextInput.error = null
        phoneTextInput.error = null
        reasonsForCallEditText.setText("")
        binding.scrollView.smoothScrollTo(0, 0)
    }

    private fun showDatePicker() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTitleText(getString(R.string.select_date))
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.MaterialCalendarTheme)
            .build()
        datePicker.show(supportFragmentManager, "date")

        datePicker.addOnPositiveButtonClickListener {
            viewModel.setDate(datePicker.selection!!)
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(viewModel.getCurrentHour())
                .setMinute(viewModel.getCurrentMinute())
                .setTitleText(getString(R.string.select_time))
                .build()
            timePicker.show(supportFragmentManager, "time")

            timePicker.addOnPositiveButtonClickListener {
                viewModel.setTime(timePicker.hour, timePicker.minute)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
    }
}