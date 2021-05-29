package ca.isucorp.acme.ui.calendar

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityCalendarBinding
import ca.isucorp.acme.databinding.CalendarPerMonthHeaderBinding
import ca.isucorp.acme.databinding.ItemCalendarDayBinding
import ca.isucorp.acme.model.DueTicket
import ca.isucorp.acme.util.DEFAULT_GO_BACK_ANIMATION
import ca.isucorp.acme.util.goBackWithAnimation
import ca.isucorp.acme.util.setUpInActivity
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private val viewModel: CalendarViewModel by lazy {
        ViewModelProvider(this, CalendarViewModel.Factory(application)).get(CalendarViewModel::class.java)
    }

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private val eventsAdapter = EventsAdapter()

    private lateinit var binding: ActivityCalendarBinding

    private lateinit var dueTicketsByDate: Map<LocalDate, List<DueTicket>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolBarTitle = binding.layoutSimpleAppBar.toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.due_tickets)
        binding.layoutSimpleAppBar.toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)

        viewModel.dueTickets.observe(this, {
            dueTicketsByDate = it
            binding.recyclerEvents.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            binding.recyclerEvents.adapter = eventsAdapter
            eventsAdapter.notifyDataSetChanged()

            val daysOfWeek = daysOfWeekFromLocale()
            val currentMonth = YearMonth.now()
            binding.calendarView.setup(currentMonth, currentMonth.plusMonths(120), daysOfWeek.first())
            binding.calendarView.scrollToMonth(currentMonth)

            class DayViewContainer(view: View) : ViewContainer(view) {
                lateinit var day: CalendarDay // Will be set when this container is bound.
                val binding = ItemCalendarDayBinding.bind(view)
                init {
                    view.setOnClickListener {
                        if (day.owner == DayOwner.THIS_MONTH) {
                            if (selectedDate != day.date) {
                                val oldDate = selectedDate
                                selectedDate = day.date
                                val binding = this@CalendarActivity.binding
                                binding.calendarView.notifyDateChanged(day.date)
                                oldDate?.let { binding.calendarView.notifyDateChanged(it) }
                                updateAdapterForDate(day.date)
                            }
                        }
                    }
                }
            }

            binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val textView = container.binding.textDayOfMonth
                    textView.text = day.date.dayOfMonth.toString()

                    container.binding.viewFirstEvent.background = null
                    container.binding.viewSecondEvent.background = null

                    if (day.owner == DayOwner.THIS_MONTH) {
                        textView.setTextColorRes(R.color.primaryColor)
                        container.binding.layoutCalendarDay.setBackgroundResource(
                            if (selectedDate == day.date) {
                                R.drawable.bg_selected
                            } else {
                                0
                            })

                        val tickets = dueTicketsByDate[day.date]
                        if (tickets != null) {
                            if (tickets.count() == 1) {
                                container.binding.viewSecondEvent.setBackgroundColor(
                                    binding.root.context.getColorCompat(R.color.secondaryColor))
                            } else {
                                container.binding.viewFirstEvent.setBackgroundColor(binding.root.context.getColorCompat(R.color.secondaryColor))
                                container.binding.viewSecondEvent.setBackgroundColor(binding.root.context.getColorCompat(R.color.calendar_event_indicator))
                            }
                        }
                    } else {
                        textView.setTextColorRes(R.color.disabled_grey)
                        container.binding.layoutCalendarDay.background = null
                    }
                }
            }

            class MonthViewContainer(view: View) : ViewContainer(view) {
                val legendLayout = CalendarPerMonthHeaderBinding.bind(view).legendLayout.root
            }
            binding.calendarView.monthHeaderBinder = object :
                MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = month.yearMonth
                        container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                                .uppercase(Locale.ENGLISH)
                            tv.setTextColorRes(R.color.primaryDarkColor)

                        }
                        month.yearMonth
                    }
                }
            }

            binding.calendarView.monthScrollListener = { month ->
                val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
                binding.monthYearText.text = title

                selectedDate?.let {
                    // Clear selection if we scroll to a new month.
                    selectedDate = null
                    binding.calendarView.notifyDateChanged(it)
                    updateAdapterForDate(null)
                }
            }

            binding.buttonNextMonth.setOnClickListener {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
                }
            }

            binding.buttonPreviousMonth.setOnClickListener {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
                }
            }
        })

    }

    private fun updateAdapterForDate(date: LocalDate?) {
        eventsAdapter.tickets.clear()
        eventsAdapter.tickets.addAll(dueTicketsByDate[date].orEmpty())
        eventsAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
    }
}