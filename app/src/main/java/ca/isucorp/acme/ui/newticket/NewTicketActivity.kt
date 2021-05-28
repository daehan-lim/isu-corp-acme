package ca.isucorp.acme.ui.newticket

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityNewTicketBinding
import ca.isucorp.acme.util.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class NewTicketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewTicketBinding
    private val viewModel: NewTicketViewModel by lazy {
        ViewModelProvider(this, NewTicketViewModel.Factory(application)).get(NewTicketViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectDateTextInput = binding.layoutActivityNewTicketContent.findViewById<TextInputLayout>(R.id.text_input_select_date)
        val selectDateEditText = binding.layoutActivityNewTicketContent.findViewById<MaterialAutoCompleteTextView>(R.id.edit_select_date)

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

        /*validateUserFields(binding.layoutActivitySignUpContent, this, viewModel, isSignUpScreen = true)

        val usernameEditText = binding.layoutActivitySignUpContent.findViewById<TextInputEditText>(R.id.edit_user_name)
        val passwordEditText = binding.layoutActivitySignUpContent.findViewById<TextInputEditText>(R.id.edit_password)

        binding.layoutActivitySignUpContent.findViewById<MaterialButton>(R.id.button_login).setOnClickListener {
            viewModel.register(usernameEditText.text.toString(), passwordEditText.text.toString())
        }

        viewModel.isSigningSuccessful.observe(this, {
            if(it) {
                MaterialDialog(this)
                    .title(text = getString(R.string.sign_up_successful))
                    .message(text = getString(R.string.sign_up_successful_message))
                    .cancelable(false)
                    .positiveButton(R.string.accept) {
                        *//*val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)*//*
                        finish()
                    }
                    .show()

            } else {
                MaterialDialog(this)
                    .title(text = getString(R.string.sign_up_error))
                    .message(text = getString(R.string.sign_up_error_message))
                    .positiveButton(R.string.accept) {}
                    .show()
            }
        })*/

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
            viewModel.setDate(it)
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
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