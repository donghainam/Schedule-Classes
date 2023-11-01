package namdh.dhbkhn.datn.domain;

import java.io.Serializable;
import javax.persistence.*;
import namdh.dhbkhn.datn.service.dto.class_name.ClassesInputDTO;
import namdh.dhbkhn.datn.service.dto.class_name.ClassesOutputDTO;

@Entity
@Table(name = "classes")
public class Classes extends AbstractAuditingEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "class_note")
    private String classNote;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "start_week")
    private int startWeek;

    @Column(name = "number_of_lessons")
    private int numberOfLessons;

    @Column(name = "number_of_week_study")
    private int numberOfWeekStudy;

    @Column(name = "semester")
    private String semester;

    @Column(name = "conditions")
    private int conditions;

    @Column(name = "count_week_studied")
    private int countWeekStudied;

    public Classes() {}

    public Classes(ClassesInputDTO classesInputDTO) {
        this.name = classesInputDTO.getName();
        this.classNote = classesInputDTO.getClassNote();
        this.courseCode = classesInputDTO.getCourseCode();
        this.startWeek = classesInputDTO.getStartWeek();
        this.numberOfLessons = classesInputDTO.getNumberOfLessons();
        this.numberOfWeekStudy = classesInputDTO.getNumberOfWeekStudy();
        this.semester = classesInputDTO.getSemester();
        this.conditions = classesInputDTO.getConditions();
    }

    public Classes(ClassesOutputDTO classesOutputDTO) {
        this.id = classesOutputDTO.getId();
        this.name = classesOutputDTO.getName();
        this.classNote = classesOutputDTO.getClassNote();
        this.courseCode = classesOutputDTO.getCourseCode();
        this.startWeek = classesOutputDTO.getStartWeek();
        this.numberOfLessons = classesOutputDTO.getNumberOfLessons();
        this.numberOfWeekStudy = classesOutputDTO.getNumberOfWeekStudy();
        this.semester = classesOutputDTO.getSemester();
        this.conditions = classesOutputDTO.getConditions();
        this.countWeekStudied = classesOutputDTO.getCountCondition();
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

    public int getCountWeekStudied() {
        return countWeekStudied;
    }

    public void setCountWeekStudied(int countCondition) {
        this.countWeekStudied = countCondition;
    }
}
