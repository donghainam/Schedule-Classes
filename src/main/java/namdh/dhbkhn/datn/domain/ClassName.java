package namdh.dhbkhn.datn.domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "class_name")
public class ClassName extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "class_code")
    private int classCode;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "start_week")
    private int startWeek;

    @Column(name = "number_of_lessons")
    private int numberOfLessons;

    @Column(name = "semester")
    private String semester;

    @Column(name = "condition")
    private int condition;

    @Column(name = "count_condition")
    private int countCondition;

    @Column(name = "count_week_studied")
    private int cntWeekStudied;

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

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getCountCondition() {
        return countCondition;
    }

    public void setCountCondition(int countCondition) {
        this.countCondition = countCondition;
    }

    public int getCntWeekStudied() {
        return cntWeekStudied;
    }

    public void setCntWeekStudied(int cntWeekStudied) {
        this.cntWeekStudied = cntWeekStudied;
    }
}
