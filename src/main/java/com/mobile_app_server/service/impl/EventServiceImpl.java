package com.mobile_app_server.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mobile_app_server.config.CloudDinaryConfig;
import com.mobile_app_server.dto.EventCategoryDto;
import com.mobile_app_server.dto.EventDto;
import com.mobile_app_server.dto.ResultSetQuery;
import com.mobile_app_server.repo.EventCateRepo;
import com.mobile_app_server.repo.EventRepo;
import com.mobile_app_server.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;
    private final EventCateRepo eventCateRepo;
    private final Cloudinary cloudinary;

    
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

    @Transactional
    @Override
    public void deleteEventById(Integer eventId) {
        eventCateRepo.deleteEventCateByEventId(eventId);
        eventRepo.deleteEventById(eventId);
    }

    @Override
    public void updateEvent(EventDto eventDto) {
        List<EventCategoryDto> eventCategoryDtosBefore = covertEventCate(eventCateRepo.getEventCateByEventId(eventDto.getId()));
        deleteUnselectedCate(eventDto, eventCategoryDtosBefore);
        insertNewCate(eventDto, eventCategoryDtosBefore);
        eventRepo.updateAccessory(eventDto);
    }

    private List<EventCategoryDto> covertEventCate(List<ResultSetQuery> data) {
        return data.stream()
                .map(dataMap -> new EventCategoryDto(dataMap.getId()))
                .collect(Collectors.toList());
    }

    private void deleteUnselectedCate(EventDto eventDto, List<EventCategoryDto> eventCategoryDtos) {
        eventCategoryDtos.forEach( eventCate ->{
            int cateBeforeId = eventCate.getCategoryDto().getId();
            if(eventDto.getCategories().stream().noneMatch(dto -> dto.getCategoryDto().getId() == cateBeforeId)){
                eventCateRepo.deleteEventCateByEventCateId(cateBeforeId);
            }
        });
    }

    private void insertNewCate(EventDto eventDto, List<EventCategoryDto> eventCategoryDtosBefore) {
        eventDto.getCategories().forEach(eventCate ->{
            int newCateId = eventCate.getCategoryDto().getId();
            if(eventCategoryDtosBefore.stream()
                    .noneMatch(eventCateBefore -> eventCateBefore.getCategoryDto().getId() == newCateId)){
                eventCateRepo.insertEventCate(eventDto.getId(), newCateId);
            }
        });
    }

    @Override
    public List<EventDto> getEventByUserId(Integer userId) {
        List<ResultSetQuery> resultSetQueries = eventRepo.getEventByUserId(userId);

        Map<Integer, List<ResultSetQuery>> eventMap = resultSetQueries.stream()
                .collect(Collectors.groupingBy(ResultSetQuery::getId));

        return eventMap.values().stream()
                .map(this::eventDtoBuilder)
                .collect(Collectors.toList());
    }


    public EventDto eventDtoBuilder(List<ResultSetQuery> setQueries){
        EventDto eventDto = new EventDto();
        for(ResultSetQuery resultSetQuery : setQueries){
            eventDto = EventDto.builder()
                    .id(resultSetQuery.getId())
                    .name(resultSetQuery.getName())
                    .startDate(resultSetQuery.getStartdate())
                    .startTime(resultSetQuery.getStarttime())
                    .imgUrl(resultSetQuery.getImgurl())
                    .build();
        }
        return eventDto;
    }

    @Override
    @Transactional
    public void insertEventV2(EventDto eventDto, MultipartFile file) throws IOException {

        String imgUrl = uploadFileAndGetUrl(file);
        eventDto.setId(new Random().nextInt(100000000));
        eventDto.setImgUrl(imgUrl);

        eventRepo.insertAccessory(eventDto);
        for(EventCategoryDto dto : eventDto.getCategories()){
            eventCateRepo.insertEventCate(eventDto.getId(), dto.getCategoryDto().getId());
        }
    }

    public String uploadFileAndGetUrl(MultipartFile file) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }
}
