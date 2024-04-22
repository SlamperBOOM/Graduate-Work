package com.slamperboom.backend.utils;

import com.slamperboom.backend.exceptions.errorCodes.DataCodes;
import com.slamperboom.backend.exceptions.exceptions.FileParserException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String dateFormat = "dd-MM-yyyy";

    private DateUtils(){}

    public static String formatDateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }

    public static Date parseDateFromString(String date){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new FileParserException(DataCodes.wrongFileFormat);
        }
    }
}
