package oj

import org.joda.time.DateTime

class BL {
    static Date parseDate(Date date, String time) {
        def tokenizer = new StringTokenizer(time, ":")
        def hours = Integer.parseInt(tokenizer.nextToken())
        def min = Integer.parseInt(tokenizer.nextToken())
        return new DateTime(date).withHourOfDay(hours).withMinuteOfHour(min).toDate()
    }
}
