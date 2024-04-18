package com.mobile_app_server.controller;

import com.mobile_app_server.dto.ScheduleDto;
import com.mobile_app_server.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping()
    public ResponseEntity<?> addSchedule(@ModelAttribute ScheduleDto scheduleDto){
        scheduleService.addSchedule(scheduleDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteSchedule(@RequestParam("id")Integer scheduleId){
        scheduleService.deleteSchedule(scheduleId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> updateSchedule(@ModelAttribute ScheduleDto scheduleDto){
        scheduleService.updateSchedule(scheduleDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/event")
    public ResponseEntity<?> getScheduleByEventId(@RequestParam("eventId") Integer eventId){
        return new ResponseEntity(scheduleService.getAllSchedule(eventId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getScheduleById(@RequestParam("id") Integer id){
        return new ResponseEntity<>(scheduleService.getScheduleById(id), HttpStatus.OK);
    }
}
