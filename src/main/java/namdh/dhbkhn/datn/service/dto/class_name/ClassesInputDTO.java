package namdh.dhbkhn.datn.service.dto.class_name;

public class ClassesInputDTO {

    private String name;
    private String classNote;
    private String courseCode;
    private int startWeek;
    private int numberOfLessons;
    private int numberOfWeekStudy;
    private String semester;
    private int conditions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassNote() {
        return classNote;
    }

    public void setClassNote(String classNote) {
        this.classNote = classNote;
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

    public int getNumberOfWeekStudy() {
        return numberOfWeekStudy;
    }

    public void setNumberOfWeekStudy(int numberOfWeekStudy) {
        this.numberOfWeekStudy = numberOfWeekStudy;
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
}
