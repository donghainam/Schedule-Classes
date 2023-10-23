package namdh.dhbkhn.datn.service.dto.schedule;

import namdh.dhbkhn.datn.domain.enumeration.WeekDay;

public class ScheduleDTO {

    private String classroom;
    private int week;
    private WeekDay day;
    private String session;
    private String className;

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public WeekDay getDay() {
        return day;
    }

    public void setDay(WeekDay day) {
        this.day = day;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
