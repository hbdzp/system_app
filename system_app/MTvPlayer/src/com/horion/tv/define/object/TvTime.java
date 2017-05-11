package com.horion.tv.define.object;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TvTime extends SkyTvObject {
    private static final String DEFAULT_DISPLAY_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final long serialVersionUID = -4229855256620161118L;
    private transient Calendar cd = Calendar.getInstance();
    public long timestamp = 0;

    public TvTime() {
        this.cd.setTimeInMillis(System.currentTimeMillis());
        this.timestamp = this.cd.getTimeInMillis();
        this.name = formatToString(DEFAULT_DISPLAY_FORMAT);
    }

    public TvTime(int i, int i2, int i3) {
        this.cd.set(i, i2 - 1, i3);
        this.timestamp = this.cd.getTimeInMillis();
        this.name = formatToString(DEFAULT_DISPLAY_FORMAT);
    }

    public TvTime(int i, int i2, int i3, int i4, int i5, int i6) {
        this.cd.set(i, i2 - 1, i3, i4, i5, i6);
        this.timestamp = this.cd.getTimeInMillis();
        this.name = formatToString(DEFAULT_DISPLAY_FORMAT);
    }

    public TvTime(long j) {
        this.cd.setTimeInMillis(j);
        this.timestamp = j;
        this.name = formatToString(DEFAULT_DISPLAY_FORMAT);
    }

    public static void main(String[] strArr) {
        System.out.println(new TvTime().formatToString("MM-dd"));
    }

    public boolean after(TvTime tvTime) {
        afterDeserialize();
        return this.timestamp > tvTime.timestamp;
    }

    public boolean before(TvTime tvTime) {
        afterDeserialize();
        return this.timestamp < tvTime.timestamp;
    }

    protected void doAfterDeserialize() {
        if (this.cd == null) {
            this.cd = Calendar.getInstance();
            this.cd.setTimeInMillis(this.timestamp);
        }
    }

    public boolean equals(Object obj) {
        return sameday((TvTime) obj);
    }

    public String formatToString(String str) {
        return new SimpleDateFormat(str).format(new Date(this.timestamp));
    }

    public int getDay() {
        afterDeserialize();
        return this.cd.get(Calendar.DATE);
    }

    public int getDayOfWeek() {
        return this.cd.get(Calendar.DAY_OF_WEEK);
    }

    public String getDisplayName() {
        return this.name;
    }

    public TvTime getEndTimeOfTheDay() {
        return new TvTime(getYear(), getMonth(), getDay(), 23, 59, 59);
    }

    public int getHour() {
        afterDeserialize();
        return this.cd.get(Calendar.HOUR_OF_DAY);
    }

    public TvTime getIptvEndTimeOfTheDay() {
        return new TvTime(getYear(), getMonth(), getDay() + 1, 0, 0, 0);
    }

    public int getMinutes() {
        afterDeserialize();
        return this.cd.get(Calendar.MINUTE);
    }

    public int getMonth() {
        afterDeserialize();
        return this.cd.get(Calendar.MONTH) + 1;
    }

    public int getSeconds() {
        afterDeserialize();
        return this.cd.get(Calendar.SECOND);
    }

    public TvTime getStartTimeOfTheDay() {
        return new TvTime(getYear(), getMonth(), getDay(), 0, 0, 0);
    }

    public int getYear() {
        afterDeserialize();
        return this.cd.get(Calendar.YEAR);
    }

    public TvTime lastDay() {
        return new TvTime(this.timestamp - 86400000);
    }

    public TvTime nextDay() {
        return new TvTime(this.timestamp + 86400000);
    }

    public boolean sameday(TvTime tvTime) {
        boolean z = true;
        boolean z2 = getYear() == tvTime.getYear();
        boolean z3 = getMonth() == tvTime.getMonth();
        if (getDay() != tvTime.getDay()) {
            z = false;
        }
        return (z2 && z3) ? z : false;
    }
}
