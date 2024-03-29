package com.mobile_app_server.service;

import com.mobile_app_server.dto.EventDto;
import org.springframework.stereotype.Service;

@Service
public interface EventService {

    void insertEvent(EventDto eventDto);

    EventDto getEventById(Integer eventId);

    void deleteEventById(Integer eventId);

    void updateEvent(EventDto eventDto);
}
