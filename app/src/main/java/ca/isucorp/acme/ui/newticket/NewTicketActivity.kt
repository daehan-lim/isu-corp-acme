package ca.isucorp.acme.ui.newticket

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityNewTicketBinding
import ca.isucorp.acme.ui.editeticket.EditTicketViewModel
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


open class NewTicketActivity : AppCompatActivity() {
    protected open lateinit var binding: ActivityNewTicketBinding
    protected open lateinit var selectDateTextInput: TextInputLayout
    protected open lateinit var selectDateEditText: MaterialAutoCompleteTextView
    protected open lateinit var saveButton: MaterialButton
    protected open lateinit var clientNameTextInput: TextInputLayout
    protected open lateinit var clientNameEditText: TextInputEditText
    protected open lateinit var addressTextInput: TextInputLayout
    protected open lateinit var addressEditText: TextInputEditText
    protected open lateinit var phoneTextInput: TextInputLayout
    protected open lateinit var phoneEditText: TextInputEditText
    protected open lateinit var notesTextInput: TextInputLayout
    protected open lateinit var notesEditText: TextInputEditText
    protected open lateinit var reasonsForCallTextInput: TextInputLayout
    protected open lateinit var reasonsForCallEditText: TextInputEditText

    protected open val viewModel: NewTicketViewModel by lazy {
        ViewModelProvider(this, NewTicketViewModel.Factory(application)).get(NewTicketViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.layoutSimpleAppBar.toolbar
        val toolBarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.new_ticket)
        toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)

        initialize()

        saveButton.setOnClickListener {
            viewModel.addTicket(
                clientNameEditText.text.toString(),
                addressEditText.text.toString(),
                phoneEditText.text.toString(),
                notesEditText.text.toString(),
                reasonsForCallEditText.text.toString(),)
        }

    }

    protected open fun initialize() {
        selectDateTextInput = binding.layoutActivityNewTicketContent.textInputSelectDate
        selectDateEditText = binding.layoutActivityNewTicketContent.editSelectDate
        saveButton = binding.layoutActivityNewTicketContent.buttonSave
        clientNameTextInput = binding.layoutActivityNewTicketContent.textInputClientName
        clientNameEditText = binding.layoutActivityNewTicketContent.editClientName
        addressTextInput = binding.layoutActivityNewTicketContent.textInputAddress
        addressEditText = binding.layoutActivityNewTicketContent.editAddress
        phoneTextInput = binding.layoutActivityNewTicketContent.textInputPhone
        phoneEditText = binding.layoutActivityNewTicketContent.editPhone
        notesTextInput = binding.layoutActivityNewTicketContent.textInputNotes
        notesEditText = binding.layoutActivityNewTicketContent.editNotes
        reasonsForCallTextInput = binding.layoutActivityNewTicketContent.textInputReasons
        reasonsForCallEditText = binding.layoutActivityNewTicketContent.editReasons

        selectDateEditText.setOnClickListener {
            showDatePicker()
        }

        selectDateTextInput.setEndIconOnClickListener {
            showDatePicker()
        }

        viewModel.dateText.observe(this, {
            selectDateEditText.setText(it)
        })


        viewModel.newTicketFormState.observe(this, Observer {
            val formState = it ?: return@Observer

            if (formState.isTicketAdded) {
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

            clientNameTextInput.error = when (formState.clientNameError) {
                null -> null
                else -> getString(formState.clientNameError)
            }
            addressTextInput.error = when (formState.addressError) {
                null -> null
                else -> getString(formState.addressError)
            }
            selectDateTextInput.error = when (formState.dateError) {
                null -> null
                else -> getString(formState.dateError)
            }
            phoneTextInput.error = when (formState.phoneError) {
                null -> null
                else -> getString(formState.phoneError)
            }
        })
    }


    protected fun resetForm() {
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

    protected fun showDatePicker() {
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