package com.nova.sme.sme01.miscellanea;

/*
 **************************************************
 *                                                *
 *   Supports simple calendar                     *
 *                                                *
 **************************************************
 */

import android.app.Activity;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCalendar {
    private Activity activity  = null;
    private Spinner  year_spinner;
    private Spinner  month_spinner;
    private Spinner  day_spinner;


    private int selected_year  = getCurrentYear();
    private int selected_month = getCurrentMonth();
    private int selected_day   = getCurrentDay();

    public SimpleCalendar() {

    }
    public SimpleCalendar(Activity activity, Spinner year_spinner, Spinner month_spinner, Spinner day_spinner) {
        this.activity      = activity;
        this.year_spinner  = year_spinner;
        this.month_spinner = month_spinner;
        this.day_spinner   = day_spinner;

        List<String> list = new ArrayList<String>();
        // YEARS
        int year = this.getCurrentYear();
        list.add(Integer.toString(year - 1));
        list.add(Integer.toString(year));
        list.add(Integer.toString(year + 1));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (activity, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(dataAdapter);
        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                setSelectedYear(position + 1);
                set_days(selected_year, selected_month);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // MONTHS
        int month = this.getCurrentMonth();
        dataAdapter = new ArrayAdapter<String> (activity, android.R.layout.simple_spinner_item, this.months);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(dataAdapter);
        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                setSelectedMonth(position + 1);
                set_days(selected_year, selected_month);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // DAYS
        set_days(this.getCurrentYear(), this.getCurrentMonth());

        int curr_day   = getCurrentDay();
        int curr_month = getCurrentMonth();
        int curr_year  = getCurrentYear();

//        year_spinner.setSelection();

    }

    private void set_days(int year, int month) {
        int day  = this.getCurrentDay();
        int days = get_days_in_month(year, month);

        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= days; i ++)
            list.add(Integer.toString(i));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (activity, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.day_spinner.setAdapter(dataAdapter);
        this.day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                setSelectedDay(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
/*
        if (selected_day > 28)
            if (selected_day > days)
                selected_day = days;

        this.day_spinner.setId(selected_day);
*/
    }
    private void correct_delected_day() {

    }

    public void setSelectedYear(int year) {
        this.selected_year = year;
    }
    public void setSelectedMonth(int month) {
        this.selected_month = month;
    }
    public void setSelectedDay(int day) {
        this.selected_day = day;
    }

    public int getSelectedYear() {
        return this.selected_year;
    }
    public int getSelectedMonth() {
        return this.selected_month;
    }
    public int getSelectedDay() {
        return this.selected_day;
    }

    public  String      months[] = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "Augustus",
            "September",
            "October",
            "November",
            "December"
    };
    private int Month[] = {
            -1,
            Calendar.JANUARY,
            Calendar.FEBRUARY,
            Calendar.MARCH,
            Calendar.APRIL,
            Calendar.MAY,
            Calendar.JUNE,
            Calendar.JULY,
            Calendar.AUGUST,
            Calendar.SEPTEMBER,
            Calendar.OCTOBER,
            Calendar.NOVEMBER,
            Calendar.DECEMBER,
    };

    public int get_days_in_month(int year, int month) {// february only problem
        GregorianCalendar calendar = new GregorianCalendar(year, Month[month], 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    public int getCurrentDay() {
        today td = new today();
        return td.day;
    }
    public int getCurrentMonth() {
        today td = new today();
        return td.month + 1;
    }
    public int getCurrentYear() {
        today td = new today();
        return td.year;
    }

    private class today {
        public int day;
        public int month;
        public int year;

        today() {
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();

            this.day   = today.monthDay;
            this.month = today.month;
            this.year  = today.year;
        }
    }

}
