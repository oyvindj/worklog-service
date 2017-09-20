package oj

import oj.beans.Work
import org.joda.time.DateTime
import org.joda.time.Duration

class BL {
    static Date parseDate(Date date, String time) {
        def tokenizer = new StringTokenizer(time, ":")
        def hours = Integer.parseInt(tokenizer.nextToken())
        def min = Integer.parseInt(tokenizer.nextToken())
        return new DateTime(date).withHourOfDay(hours).withMinuteOfHour(min).toDate()
    }
    static String getDuration(Work work) {
        def dur = new Duration(work.fromDateTime.time, work.toDateTime.time)
        def min = dur.standardMinutes - (dur.standardHours * 60)
        return dur.standardHours + "h " + min + 'm'
    }
}
