/*
   Copyright 2011 Sander Kooijmans

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package nl.gogognome.lib.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class contains utility methods for <code>Date</code>s.
 *
 * @author Sander Kooijmans
 */
public class DateUtil {

	private final static DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

    /** Calendar used to perform conversions. */
    private final static Calendar CALENDAR = Calendar.getInstance();

    /**
     * Creates a date with the time set to midnight of the date.
     * @param year the year
     * @param month the month (1 = January, ..., 12 = December)
     * @param day the day
     * @return the date
     */
    public static Date createDate(int year, int month, int day) {
    	return createDate(year, month, day, 0, 0, 0, 0);
    }

    /**
     * Creates a date including a time. The number of milliseconds is set to zero.
     * @param year the year
     * @param month the month (1 = January, ..., 12 = December)
     * @param day the day
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     * @return the date
     */
    public static Date createDate(int year, int month, int day, int hour, int minute, int second) {
    	return createDate(year, month, day, hour, minute, second, 0);
    }

    /**
     * Creates a date including a time.
     * @param year the year
     * @param month the month (1 = January, ..., 12 = December)
     * @param day the day
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     * @param millisecond the millisecond
     * @return the date
     */
    public static Date createDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
    	synchronized (CALENDAR) {
    		CALENDAR.set(year, month - 1, day, hour, minute, second);
    		CALENDAR.set(Calendar.MILLISECOND, millisecond);
    		return CALENDAR.getTime();
    	}
    }

    /**
     * Compares two dates up to the day of year, ignoring their time within
     * the day.
     *
     * @param date1 a date
     * @param date2 a date
     * @return a negative integer if <code>date1</code> comes before
     *          <code>date2</code>;0 if <code>date1</code> represents the same day
     *          as <code>date2</code>; a positive integer if <code>date1</code>
     *          comes after <code>date2</code>.
     */
    public static int compareDayOfYear(Date date1, Date date2) {
        synchronized(CALENDAR) {
		    CALENDAR.setTime(date1);
		    int year1 = CALENDAR.get(Calendar.YEAR);
		    int dayOfYear1 = CALENDAR.get(Calendar.DAY_OF_YEAR);

		    CALENDAR.setTime(date2);
		    int year2 = CALENDAR.get(Calendar.YEAR);
		    int dayOfYear2 = CALENDAR.get(Calendar.DAY_OF_YEAR);

		    if (year1 < year2) {
		        return -1;
		    } else if (year1 > year2) {
		        return 1;
		    }
		    if (dayOfYear1 < dayOfYear2) {
		        return -1;
		    } else if (dayOfYear1 > dayOfYear2) {
		        return 1;
		    }

		    return 0;
        }
    }

    /**
     * Gets a field of the date.
     * @param date the date
     * @param field the field (the same value as for <code>Calendar.get(int)</code>)
     * @return the value of the field
     */
    public static int getField(Date date, int field) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            return CALENDAR.get(field);
        }
    }

    /**
     * Adds a number of days to a <code>Date</code>.
     * @param date the <code>Date</code>
     * @param numDays the number of days
     * @return the new date
     */
    public static Date addDays(Date date, int numDays) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            CALENDAR.add(Calendar.DATE, numDays);
            return CALENDAR.getTime();
        }
    }

    /**
     * Adds a number of days to a <code>Date</code>.
     * @param date the <code>Date</code>
     * @param numMonths the number of days
     * @return the new date
     */
    public static Date addMonths(Date date, int numMonths) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            CALENDAR.add(Calendar.MONTH, numMonths);
            return CALENDAR.getTime();
        }
    }

    /**
     * Adds a number of years to a <code>Date</code>.
     * @param date the <code>Date</code>
     * @param numYears the number of years
     * @return the new date
     */
    public static Date addYears(Date date, int numYears) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            CALENDAR.add(Calendar.YEAR, numYears);
            return CALENDAR.getTime();
        }
    }

    /**
     * Gets the difference between two dates in years.
     *
     * @param date1 a date
     * @param date2 another date
     * @return date1 minus date2 expressed in years
     */
    public static int getDifferenceInYears(Date date1, Date date2) {
        int year1 = getField(date1, Calendar.YEAR);
        int month1 = getField(date1, Calendar.MONTH);
        int year2 = getField(date2, Calendar.YEAR);
        int month2 = getField(date2, Calendar.MONTH);

        int diffInYears = year1 - year2;
        if (month1 < month2) {
            diffInYears--;
        } else if (month2 == month1) {
            int day1 = getField(date1, Calendar.DATE);
            int day2 = getField(date2, Calendar.DATE);
            if (day1 < day2) {
                diffInYears--;
            }
        }
        return diffInYears;
    }

    /**
     * Formats a date in the format YYYYMMDD.
     * @param date the date
     * @return the formatted date or the empty string if date == null.
     */
    public static String formatDateYYYYMMDD(Date date) {
    	if (date != null) {
	    	synchronized (YYYYMMDD) {
	    		return YYYYMMDD.format(date);
	    	}
    	} else {
    		return "";
    	}
    }

    /**
     * Parses a date in the format YYYYMMDD. Contrary to {@link DateFormat#parse(String)}
     * parses the complete string and not just a prefix.
     * @param dateString the string to be parsed
     * @return the date
     * @throws ParseException if the parameter does not match the format YYYYMMDD.
     */
	public static Date parseDateYYYYMMDD(String dateString) throws ParseException {
		synchronized (YYYYMMDD) {
			Date date = YYYYMMDD.parse(dateString);
			if (!YYYYMMDD.format(date).equals(dateString)) {
				throw new ParseException("The date \"" + dateString + "\" is not in the format YYYYMMDD." , 1);
			}
			return date;
		}
	}
}
