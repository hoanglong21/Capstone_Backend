package com.capstone.project.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DateRangePicker {
    public List<String> getDateRange() {
        // Get the current date
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<String> listOfDate = new ArrayList<>();
        listOfDate.add(dateFormat.format(currentDate));
        for (int i = 0; i < 12; i++) {
            // Subtract 7 days
            calendar.add(Calendar.DAY_OF_MONTH, -7);

            // Get the updated date as a Date object
            Date updatedDate = calendar.getTime();

            // Now currentDate contains the date 7 days ago from today
            listOfDate.add(dateFormat.format(updatedDate));
        }
        Collections.reverse(listOfDate);

        return listOfDate;
    }

    public List<String> getShortDateRange() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<String> listOfDate = new ArrayList<>();

        // Subtract 30 days
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        // Get the updated date as a Date object
        Date updatedDate = calendar.getTime();

        listOfDate.add(dateFormat.format(updatedDate));
        listOfDate.add(dateFormat.format(currentDate));

        return listOfDate;
    }

    public List<String> getDateActive() {
        List<String> listOfDate = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // Find the most recent Sunday
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysToPreviousSunday = (currentDayOfWeek == Calendar.SUNDAY) ? 0 : (Calendar.SUNDAY - currentDayOfWeek);
        calendar.add(Calendar.DAY_OF_MONTH, daysToPreviousSunday);

        // Add the previous four Sundays
        for (int i = 0; i < 3; i++) {
            // Subtract 7 days to get the previous Sunday
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        }
        Date start = calendar.getTime();
        listOfDate.add(dateFormat.format(start));

        // Find the most recent Saturday
        calendar = Calendar.getInstance();
        currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysToNextSaturday = (currentDayOfWeek == Calendar.SATURDAY) ? 0 : (Calendar.SATURDAY - currentDayOfWeek);
        calendar.add(Calendar.DAY_OF_MONTH, daysToNextSaturday);

        // Add the next two Saturdays
        for (int i = 0; i < 2; i++) {
            // Add 7 days to get the next Saturday
            calendar.add(Calendar.DAY_OF_MONTH, 7);
        }
        Date end = calendar.getTime();

        // Add all the dates between start and end (inclusive)
        calendar.setTime(start);
        while (calendar.getTime().before(end)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            listOfDate.add(dateFormat.format(calendar.getTime()));
        }
//        int index = listOfDate.size() - 1;
//        listOfDate.remove(index);

        return listOfDate;
    }

    public List<String[]> getRecent4Weeks() {
        List<String[]> listOfDateRanges = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        for (int i = 0; i < 4; i++) {

            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int daysToNextSaturday = (Calendar.SATURDAY - currentDayOfWeek + 1) % 7;
            calendar.add(Calendar.DAY_OF_MONTH, daysToNextSaturday);
            Date end = calendar.getTime();


            calendar.add(Calendar.DAY_OF_MONTH, -6);
            Date start = calendar.getTime();

            String startDateString = dateFormat.format(start);
            String endDateString = dateFormat.format(end);

            String[] dateRange = {startDateString, endDateString};
            listOfDateRanges.add(dateRange);

            calendar.add(Calendar.DAY_OF_MONTH, -7);
        }

        Collections.reverse(listOfDateRanges);
        return listOfDateRanges;
    }
}

