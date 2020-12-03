package com.example.rahultyagi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.customecalender.OnDateSelectedListener
import com.example.customecalender.activity.DatePickerTimeline
import com.example.testlib.R
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val datePickerTimeline: DatePickerTimeline = findViewById(R.id.dateTimeline)
        datePickerTimeline.setInitialDate(2019, 10, 12)
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_YEAR, 5)
        datePickerTimeline.setActiveDate(date)
        datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                //Do Something
                Log.d(TAG, "onDateSelected: $day")
            }

            override fun onDisabledDateSelected(
                year: Int,
                month: Int,
                day: Int,
                dayOfWeek: Int,
                isDisabled: Boolean
            ) {
                Log.d(TAG, "onDisabledDateSelected: $day")
            }
        })
        val dates =
            arrayOf(Calendar.getInstance().time)
        datePickerTimeline.deactivateDates(dates)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
