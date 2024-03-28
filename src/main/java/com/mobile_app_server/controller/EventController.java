package com.mobile_app_server.controller;

import com.mobile_app_server.dto.EventDto;
import com.mobile_app_server.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping()
    public ResponseEntity<?> insertEvent(@RequestBody EventDto eventDto){
        eventService.insertEvent(eventDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getEventById(@RequestParam("id") Integer eventId){
        EventDto result = eventService.getEventById(eventId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
