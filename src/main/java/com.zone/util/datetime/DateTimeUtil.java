package com.zone.util.datetime;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName DateTimeUtil
 * @Author zone
 * @Date 2019/1/12  17:29
 * @Version 1.0
 * @Description
 */
public class DateTimeUtil {
    public static Calendar timestampToCalendar(Timestamp timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        return calendar;
    }
    public static Date calendarToDate(Calendar calendar){
        Date date = calendar.getTime();
        return date;
    }
    public static Timestamp calendarToTimestamp(Calendar calendar){
        return dateToTimestamp(calendarToDate(calendar));
    }
    public static Timestamp dateToTimestamp(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(df.format(date));
    }
}
