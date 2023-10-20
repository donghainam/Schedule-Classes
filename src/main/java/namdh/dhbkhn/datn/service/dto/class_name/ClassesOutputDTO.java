package namdh.dhbkhn.datn.service.dto.class_name;

import namdh.dhbkhn.datn.domain.Classes;

public class ClassesOutputDTO {

    private Long id;
    private String name;
    private int classCode;
    private String courseCode;
    private int startWeek;
    private int numberOfLessons;
    private String semester;
    private int conditions;
    private int countCondition;
    private int countWeekStudied;

    public ClassesOutputDTO() {}

    public ClassesOutputDTO(Classes classes) {
        this.id = classes.getId();
        this.name = classes.getName();
        this.classCode = classes.getClassCode();
        this.courseCode = classes.getCourseCode();
        this.startWeek = classes.getStartWeek();
        this.numberOfLessons = classes.getNumberOfLessons();
        this.semester = classes.getSemester();
        this.conditions = classes.getConditions();
        this.countCondition = classes.getCountCondition();
        this.countWeekStudied = classes.getCountWeekStudied();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassCode() {
        return classCode;
    }

    public void setClassCode(int classCode) {
        this.classCode = classCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getNumberOfLessons() {
        return numberOfLessons;
    }

    public void setNumberOfLessons(int numberOfLessons) {
        this.numberOfLessons = numberOfLessons;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getConditions() {
        return conditions;
    }

    public void setConditions(int conditions) {
        this.conditions = conditions;
    }

    public int getCountCondition() {
        return countCondition;
    }

    public void setCountCondition(int countCondition) {
        this.countCondition = countCondition;
    }

    public int getCountWeekStudied() {
        return countWeekStudied;
    }

    public void setCountWeekStudied(int countWeekStudied) {
        this.countWeekStudied = countWeekStudied;
    }
}
