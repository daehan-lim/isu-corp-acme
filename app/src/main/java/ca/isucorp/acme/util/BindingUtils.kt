package ca.isucorp.acme.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ca.isucorp.acme.R
import ca.isucorp.acme.database.model.Ticket
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("clientName")
fun TextView.setClientName(item: Ticket?) {
    item?.let {
        text = item.clientName
    }
}

@BindingAdapter("address")
fun TextView.setAddress(item: Ticket?) {
    item?.let {
        text = item.address
    }
}

@BindingAdapter("date")
fun TextView.setDate(item: Ticket?) {
    item?.let {
        val dateStringParser = SimpleDateFormat(DATE_AND_TIME_PATTERN, ENGLISH_US_LOCALE)
        text = dateStringParser.format(item.date)
    }
}

@BindingAdapter("phoneText")
fun TextView.setPhoneText(item: Ticket?) {
    item?.let {
        text = item.phone
    }
}

@BindingAdapter("phoneVisibility")
fun View.setPhoneVisibility(item: Ticket?) {
    item?.let {
        visibility = if(item.phone.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}

