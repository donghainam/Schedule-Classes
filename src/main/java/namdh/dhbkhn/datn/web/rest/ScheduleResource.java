package namdh.dhbkhn.datn.web.rest;

import java.io.IOException;
import namdh.dhbkhn.datn.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleResource {

    private final ScheduleService scheduleService;

    public ScheduleResource(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /*@GetMapping("")
    public void schedule() {
        scheduleService.generateGreedySchedule();
    }*/

    @GetMapping(value = "/excel", produces = { "application/vnd.ms-excel" })
    public ResponseEntity exportSchedule() {
        byte[] bytes = scheduleService.exportSchedule();
        return ResponseEntity
            .ok()
            .header("content-disposition", "attachment; filename=Schedule.xlsx")
            .header("Pragma", "public")
            .header("Cache-Control", "no-store")
            .header("Cache-Control", "max-age=0")
            .contentLength(bytes.length)
            .body(bytes);
    }
}
