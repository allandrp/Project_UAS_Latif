package com.example.project_uas.model;

public class Event {
    String title;
    int dateStart,
            monthStart,
            yearStart,
            hourStart,
            minuteStart,
            secondStart,
            dateEnd,
            monthEnd,
            yearEnd,
            hourEnd,
            minuteEnd,
            secondEnd;

    public Event(String title, int dateStart, int monthStart, int yearStart) {
        this.title = title;
        this.dateStart = dateStart;
        this.monthStart = monthStart;
        this.yearStart = yearStart;
    }

    public Event() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateStart(int dateStart) {
        this.dateStart = dateStart;
    }

    public void setMonthStart(int monthStart) {
        this.monthStart = monthStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }

    public void setHourStart(int hourStart) {
        this.hourStart = hourStart;
    }

    public void setMinuteStart(int minuteStart) {
        this.minuteStart = minuteStart;
    }

    public void setSecondStart(int secondStart) {
        this.secondStart = secondStart;
    }

    public void setDateEnd(int dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setMonthEnd(int monthEnd) {
        this.monthEnd = monthEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }

    public void setHourEnd(int hourEnd) {
        this.hourEnd = hourEnd;
    }

    public void setMinuteEnd(int minuteEnd) {
        this.minuteEnd = minuteEnd;
    }

    public void setSecondEnd(int secondEnd) {
        this.secondEnd = secondEnd;
    }

    public int getDateStart() {
        return dateStart;
    }

    public int getMonthStart() {
        return monthStart;
    }

    public int getYearStart() {
        return yearStart;
    }

    public int getHourStart() {
        return hourStart;
    }

    public int getMinuteStart() {
        return minuteStart;
    }

    public int getSecondStart() {
        return secondStart;
    }

    public int getDateEnd() {
        return dateEnd;
    }

    public int getMonthEnd() {
        return monthEnd;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public int getHourEnd() {
        return hourEnd;
    }

    public int getMinuteEnd() {
        return minuteEnd;
    }

    public int getSecondEnd() {
        return secondEnd;
    }
}
