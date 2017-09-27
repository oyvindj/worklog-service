package oj.util

import org.joda.time.LocalDate

class DateUtil {
    static Date getStartOfCurrentWeek() {
        def week = new LocalDate().getWeekOfWeekyear()
        return getStartOfWeek(week)
    }
    static Date getStartOfWeek(int week) {
        def day = new LocalDate().withWeekOfWeekyear(week).dayOfWeek().withMinimumValue().toDateTimeAtStartOfDay().toDate()
        return day
    }
    static Date getEndOfCurrentWeek() {
        def week = new LocalDate().getWeekOfWeekyear()
        return getEndOfWeek(week)
    }
    static Date getEndOfWeek(int week) {
        def day = new LocalDate().withWeekOfWeekyear(week).dayOfWeek().withMaximumValue().toDateTimeAtStartOfDay().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate()
        return day
    }

    static void main(String[] args) {
        println getStartOfWeek(1)
        println getEndOfWeek(1)
        println getStartOfCurrentWeek()
        println getEndOfCurrentWeek()
    }
}
