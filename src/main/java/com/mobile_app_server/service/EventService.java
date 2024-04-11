package com.mobile_app_server.service;

import com.mobile_app_server.dto.EventCategoryDto;
import com.mobile_app_server.dto.EventDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface EventService {

    EventDto getEventById(Integer eventId);

    void deleteEventById(Integer eventId);

    void updateEvent(EventDto eventDto);

    List<EventDto> getEventByUserId(Integer userId);

    void insertEventV2(EventDto eventDto, MultipartFile file) throws IOException;
}
