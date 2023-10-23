package namdh.dhbkhn.datn.service.dto.classroom;

import namdh.dhbkhn.datn.domain.Classroom;

public class ClassroomOutputDTO {

    private Long id;
    private String name;

    public ClassroomOutputDTO() {}

    public ClassroomOutputDTO(Classroom classroom) {
        this.id = classroom.getId();
        this.name = classroom.getName();
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
}
