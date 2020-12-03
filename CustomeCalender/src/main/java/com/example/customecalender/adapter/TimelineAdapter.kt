package com.example.customecalender.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customecalender.OnDateSelectedListener
import com.example.customecalender.R
import com.example.customecalender.TimelineView
import java.text.DateFormatSymbols
import java.util.*

/**
 * Created by Rahul on 03/12/20.
 */
class TimelineAdapter(timelineView: TimelineView, selectedPosition: Int) :
    RecyclerView.Adapter<TimelineAdapter.ViewHolder?>() {
    private val calendar = Calendar.getInstance()
    private val timelineView: TimelineView
    private lateinit var deactivatedDates: Array<Date?>
    private var listener: OnDateSelectedListener? = null
    private var selectedView: View? = null
    private var selectedPosition: Int
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.timeline_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        resetCalendar()
        calendar.add(Calendar.DAY_OF_YEAR, position)
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val isDisabled = holder.bind(month, day, dayOfWeek, year, position)
        holder.rootView.setOnClickListener { v ->
            if (selectedView != null) {
                selectedView!!.background = null
            }
            if (!isDisabled) {
                v.background = timelineView.getResources().getDrawable(R.drawable.background_shape)
                selectedPosition = position
                selectedView = v
                if (listener != null) listener?.onDateSelected(year, month, day, dayOfWeek)
            } else {
                if (listener != null) listener?.onDisabledDateSelected(
                    year,
                    month,
                    day,
                    dayOfWeek,
                    isDisabled
                )
            }
        }
    }

    private fun resetCalendar() {
        calendar[timelineView.getYear(), timelineView.getMonth(), timelineView.getDate(), 1, 0] = 0
    }

    /**
     * Set the position of selected date
     * @param selectedPosition active date Position
     */
    fun setSelectedPosition(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    fun disableDates(dates: Array<Date?>) {
        deactivatedDates = dates
        notifyDataSetChanged()
    }

    fun setDateSelectedListener(listener: OnDateSelectedListener?) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val monthView: TextView
        private val dateView: TextView
        private val dayView: TextView
        val rootView: View
        fun bind(month: Int, day: Int, dayOfWeek: Int, year: Int, position: Int): Boolean {
            monthView.setTextColor(timelineView.getMonthTextColor())
            dateView.setTextColor(timelineView.getDateTextColor())
            dayView.setTextColor(timelineView.getDayTextColor())
            dayView.text = WEEK_DAYS[dayOfWeek].toUpperCase(Locale.US)
            monthView.text = MONTH_NAME[month].toUpperCase(Locale.US)
            dateView.text = day.toString()
            if (selectedPosition == position) {
                rootView.background =
                    timelineView.getResources().getDrawable(R.drawable.background_shape)
                selectedView = rootView
            } else {
                rootView.background = null
            }
            for (date in deactivatedDates) {
                val tempCalendar = Calendar.getInstance()
                tempCalendar.time = date
                if (tempCalendar[Calendar.DAY_OF_MONTH] == day && tempCalendar[Calendar.MONTH] == month && tempCalendar[Calendar.YEAR] == year
                ) {
                    monthView.setTextColor(timelineView.getDisabledDateColor())
                    dateView.setTextColor(timelineView.getDisabledDateColor())
                    dayView.setTextColor(timelineView.getDisabledDateColor())
                    rootView.background = null
                    return true
                }
            }
            return false
        }

        init {
            monthView = itemView.findViewById(R.id.monthView)
            dateView = itemView.findViewById(R.id.dateView)
            dayView = itemView.findViewById(R.id.dayView)
            rootView = itemView.findViewById(R.id.rootView)
        }
    }

    companion object {
        private const val TAG = "TimelineAdapter"
        private val WEEK_DAYS =
            DateFormatSymbols.getInstance().shortWeekdays
        private val MONTH_NAME =
            DateFormatSymbols.getInstance().shortMonths
    }

    init {
        this.timelineView = timelineView
        this.selectedPosition = selectedPosition
    }
}