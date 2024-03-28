package com.mobile_app_server.service.impl;

import com.mobile_app_server.dto.CategoryDto;
import com.mobile_app_server.dto.EventCategoryDto;
import com.mobile_app_server.dto.EventDto;
import com.mobile_app_server.dto.ResultSetQuery;
import com.mobile_app_server.repo.EventCateRepo;
import com.mobile_app_server.repo.EventRepo;
import com.mobile_app_server.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;
    private final EventCateRepo eventCateRepo;

    @Transactional
    @Override
    public void insertEvent(EventDto eventDto) {
        eventDto.setId(new Random().nextInt(100000000));
        eventRepo.insertAccessory(eventDto);
        for(EventCategoryDto dto : eventDto.getCategories()){
            eventCateRepo.insertEventCate(eventDto.getId(), dto.getCategoryDto().getId());
        }
    }

    @Override
    public EventDto getEventById(Integer eventId) {
        List<ResultSetQuery> data = eventRepo.getEventById(eventId);
        return convertData(data);
    }

    private EventDto convertData(List<ResultSetQuery> data) {
        EventDto eventDto = new EventDto(data.get(0));

        List<EventCategoryDto> eventCategoryDtos = data.stream()
                .map(EventCategoryDto::new)
                .collect(Collectors.toList());
        eventDto.setCategories(eventCategoryDtos);

        return eventDto;
    }
}
