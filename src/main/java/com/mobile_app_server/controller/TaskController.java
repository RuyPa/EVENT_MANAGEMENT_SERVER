package com.mobile_app_server.controller;

import com.mobile_app_server.dto.ScheduleDto;
import com.mobile_app_server.dto.TaskDto;
import com.mobile_app_server.service.ScheduleService;
import com.mobile_app_server.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<?> addSchedule(@ModelAttribute TaskDto taskDto){
        taskService.addTask(taskDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteTask(@RequestParam("id")Integer taskId){
        taskService.deleteTask(taskId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> updateTask(@ModelAttribute TaskDto taskDto){
        taskService.updateTask(taskDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/event")
    public ResponseEntity<?> getTaskByEventId(@RequestParam("eventId") Integer eventId){
        return new ResponseEntity(taskService.getAllTask(eventId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getTaskById(@RequestParam("id") Integer id){
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @PutMapping("/update-checked")
    public ResponseEntity<?> updateChecked(@ModelAttribute TaskDto taskDto){
        taskService.updateChecked(taskDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
