package wsy.org.mytestapplication.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by wsy on 2017/6/29.
 */

public class CalendarUtil {

    /**
     * 求日期间隔
     */
    public static int getBetweenDays(long bingTime, long smallTime) {
        if (bingTime < smallTime) {
            throw new IllegalArgumentException("bingTime must not less than smallTime");
        }
        int days = 0;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTimeInMillis(bingTime);
        calendar2.setTimeInMillis(smallTime);
        days = calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        if (year1 == year2) {
            return days;
        } else {
            while (year1 != year2) {
                days += calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
                calendar2.add(Calendar.YEAR, 1);
                year2 = calendar2.get(Calendar.YEAR);
            }
            return days;
        }
    }

    /**
     * 获取日期的毫秒数
     */
    public static long getMillonsecond(String dateStr, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Date date = simpleDateFormat.parse(dateStr);
        gregorianCalendar.setTime(date);
        return gregorianCalendar.getTimeInMillis();
    }

    /**
     * 根据毫秒数得到日期
     */
    public static String getFormatDate(long time, String format) {
        Date date = new Date(time);// yyyy-MM-dd HH:mm:ss
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(gregorianCalendar.getTime());
    }

}
