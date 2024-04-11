package com.mobile_app_server.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mobile_app_server.dto.EventDto;
import com.mobile_app_server.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping()
    public ResponseEntity<?> getEventById(@RequestParam("id") Integer eventId){
        EventDto result = eventService.getEventById(eventId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteEventById(@RequestParam("id")Integer eventId){
        eventService.deleteEventById(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping ()
    public ResponseEntity<?> updateEvent(@RequestBody EventDto eventDto){
        eventService.updateEvent(eventDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/my-event")
    public ResponseEntity<?> getEventsByUserId(@RequestParam("userId") Integer userId){
        return new ResponseEntity<>(eventService.getEventByUserId(userId), HttpStatus.OK);
    }


    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertEventV2(@RequestParam("file") MultipartFile file,
                                           @ModelAttribute EventDto eventDto) throws IOException {
        eventService.insertEventV2(eventDto, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
