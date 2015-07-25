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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class SimpleCalendar {
    private Activity activity  = null;
    private Spinner  year_spinner;
    private Spinner  month_spinner;
    private Spinner  day_spinner;

    private String selectedYear;
    private String selectedMonth;
    private String selectedDay;

    private int    year_curr_position;
    private int    month_curr_position;
    private int    day_curr_position;

    public String getYear(){return this.selectedYear;}
    public String getMonth(){return this.selectedMonth;}
    public String getDay(){return this.selectedDay;}

    public SimpleCalendar() {

    }
    public SimpleCalendar(Activity activity, Spinner year_spinner, Spinner month_spinner, Spinner day_spinner) {
        this.activity      = activity;
        this.year_spinner  = year_spinner;
        this.month_spinner = month_spinner;
        this.day_spinner   = day_spinner;

        List<String> list = new ArrayList<String>();
        // YEARS
        int year = this.getCurrentYear();selectedYear = Integer.toString(year);
        list.add(Integer.toString(year - 1));
        list.add(Integer.toString(year));
        list.add(Integer.toString(year + 1));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (activity, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(dataAdapter);
        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                year_curr_position = position;
                TextView tv = (TextView) v;
                if (tv != null) {
                    selectedYear = tv.getText().toString();
                    correct_days();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // MONTHS
        int month = this.getCurrentMonth(); selectedMonth = months[month];
        dataAdapter = new ArrayAdapter<String> (activity, android.R.layout.simple_spinner_item, this.months);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(dataAdapter);
        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                month_curr_position = position;
                TextView tv = (TextView) v;
                if (tv != null) {
                    selectedMonth = tv.getText().toString();
                    correct_days();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // DAYS
        setDays(get_days_in_month(year, month));
        int curr_day   = getCurrentDay();
        int curr_month = getCurrentMonth();
        int curr_year  = getCurrentYear();
        int index;

        index = getIndex(day_spinner, Integer.toString(curr_day));
        if (index != -1)
            day_spinner.setSelection(index);

        index = getIndex(month_spinner, months[curr_month]);
        if (index != -1)
            month_spinner.setSelection(index);

        index = getIndex(year_spinner, Integer.toString(curr_year));
        if (index != -1)
            year_spinner.setSelection(index);


    }

    public String getDateFormatted() {//dd/mm/yyyy
        String date = null;

        String day;
        if (day_curr_position + 1 < 10)
            day = "0" + Integer.toString(day_curr_position + 1);
        else
            day = Integer.toString(day_curr_position + 1);

        String month;
        if (month_curr_position + 1 < 10)
            month = "0" + Integer.toString(month_curr_position + 1);
        else
            month = Integer.toString(month_curr_position + 1);

        String year = selectedYear;

        date = day + "/" + month + "/" + year;

        return date;
    }
    private void setDays(int days) {
//        int days        = get_days_in_month(Integer.parseInt(this.selectedYear), month_curr_position);
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= days; i ++)
            list.add(Integer.toString(i));

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String> (activity, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.day_spinner.setAdapter(dataAdapter);
        this.day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                TextView tv       = (TextView) v;
                if (tv != null) {
                    selectedDay = tv.getText().toString();
                    day_curr_position = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    void correct_days() {
        if (this.selectedDay == null)
            return;
        if (this.selectedMonth == null)
            return;
        if (this.selectedYear == null)
            return;

        int current_day = Integer.parseInt(this.selectedDay);
        int days        = get_days_in_month(Integer.parseInt(this.selectedYear), month_curr_position);
        setDays(days);

        if (current_day > days)
            day_spinner.setSelection(days - 1);
        else
            day_spinner.setSelection(current_day - 1);
    }

    private int getIndex(Spinner spinner, String val) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)spinner.getAdapter();
        int cnt = adapter.getCount();
        for (int i = 0; i < cnt; i ++)
            if (adapter.getItem(i).toString().equals(val))
                return i;
        return -1;
    }
    private String getValue(int index, Spinner spinner) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)spinner.getAdapter();
        return adapter.getItem(index);
    }


    public  String      months[] = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };
    private int Month[] = {
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
        return td.month;
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
