package namdh.dhbkhn.datn.service.dto.class_room;

import namdh.dhbkhn.datn.domain.Classroom;

public class ClassRoomOutputDTO {

    private Long id;
    private String name;

    public ClassRoomOutputDTO() {}

    public ClassRoomOutputDTO(Classroom classRoom) {
        this.id = classRoom.getId();
        this.name = classRoom.getName();
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
