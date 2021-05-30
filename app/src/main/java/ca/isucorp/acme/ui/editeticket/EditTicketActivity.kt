package ca.isucorp.acme.ui.editeticket

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityNewTicketBinding
import ca.isucorp.acme.ui.newticket.NewTicketActivity
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

        

    }



}