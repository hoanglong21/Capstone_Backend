package com.capstone.project.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class UserSettingValidation {
    public static boolean isValidTimeFormat(String timeString) {
        Pattern timePattern = Pattern.compile("[0-9]{2}:[0-9]{2}");

        // Check if the time string matches the expected format
        if (!timePattern.matcher(timeString).matches()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false); // Strict parsing
        try {
            Date date = sdf.parse(timeString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidLanguage(String language) {
        Set<String> validLanguages = new HashSet<>();
        validLanguages.add("en");
        validLanguages.add("vi");
        validLanguages.add("jp");

        return validLanguages.contains(language);
    }

    public static boolean isValidInteger(String intValue) {
        try {
            Integer.parseInt(intValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidBoolean(String booleanValue) {
        return booleanValue.equalsIgnoreCase("TRUE") || booleanValue.equalsIgnoreCase("FALSE");
    }
}
