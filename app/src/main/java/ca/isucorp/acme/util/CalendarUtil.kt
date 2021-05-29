package ca.isucorp.acme.util

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import java.util.*

fun getCalendarId(context: Context) : Long? {
    val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)

    var calCursor = context.contentResolver.query(
        CalendarContract.Calendars.CONTENT_URI,
        projection,
        CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + "=1",
        null,
        CalendarContract.Calendars._ID + " ASC"
    )

    if (calCursor != null && calCursor.count <= 0) {
        calCursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            CalendarContract.Calendars.VISIBLE + " = 1",
            null,
            CalendarContract.Calendars._ID + " ASC"
        )
    }

    if (calCursor != null) {
        if (calCursor.moveToFirst()) {
            val calName: String
            val calID: String
            val nameCol = calCursor.getColumnIndex(projection[1])
            val idCol = calCursor.getColumnIndex(projection[0])

            calName = calCursor.getString(nameCol)
            calID = calCursor.getString(idCol)

            calCursor.close()
            return calID.toLong()
        }
    }
    return null
}


fun addEventToCalendar(context: Context, title: String, description: String?, startDateMillis: Long, endDateInMillis: Long): Long? {
    val timeZone = TimeZone.getDefault()

    val values = ContentValues().apply {
        put(CalendarContract.Events.CALENDAR_ID, getCalendarId(context))
        put(CalendarContract.Events.TITLE, title)
        description?.let { put(CalendarContract.Events.DESCRIPTION, description) }
        put(CalendarContract.Events.DTSTART, startDateMillis)
        put(CalendarContract.Events.DTEND, endDateInMillis)
        put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.displayName)
    }
    val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    return uri?.lastPathSegment?.toLong()
}