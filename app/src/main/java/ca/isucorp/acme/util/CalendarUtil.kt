package ca.isucorp.acme.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import java.lang.Exception
import java.util.*

fun getCalendarId(contentResolver: ContentResolver) : Long? {
    val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)

    var calCursor = contentResolver.query(
        CalendarContract.Calendars.CONTENT_URI,
        projection,
        CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + "=1",
        null,
        CalendarContract.Calendars._ID + " ASC"
    )

    if (calCursor != null && calCursor.count <= 0) {
        calCursor = contentResolver.query(
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


fun addEventToCalendar(context: Context, title: String, description: String?, startDateMillis: Long, endDateInMillis: Long,
                       tag: String): Long? {
    val timeZone = TimeZone.getDefault()

    val contentResolver = context.contentResolver
    val calendarId = getCalendarId(contentResolver) ?: throw Exception()

    if(checkIfEventAlreadyExist(contentResolver, tag, calendarId) == null) {
        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.TITLE, title)
            description?.let { put(CalendarContract.Events.DESCRIPTION, description) }
            put(CalendarContract.Events.DTSTART, startDateMillis)
            put(CalendarContract.Events.DTEND, endDateInMillis)
            put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.displayName)
            put(CalendarContract.Events.UID_2445, tag)
        }
        val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        return uri?.lastPathSegment?.toLong()
    }
    return null
}

fun checkIfEventAlreadyExist(contentResolver: ContentResolver, tag: String, calendarId: Long): Long? {
    val projection = arrayOf(CalendarContract.Events._ID, CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND, CalendarContract.Events.UID_2445)
    val cursor = contentResolver.query(
        CalendarContract.Events.CONTENT_URI,
        projection,
        "${CalendarContract.Events.UID_2445} = ? and ${CalendarContract.Events.CALENDAR_ID} = ?",
        arrayOf(tag, calendarId.toString()),
        "${CalendarContract.Events._ID} ASC")

    cursor?.let {
        cursor.moveToFirst()
        val eventId = if (cursor.count == 0) null else cursor.getLong(0)
        cursor.close()
        return eventId
    }

    return null
}