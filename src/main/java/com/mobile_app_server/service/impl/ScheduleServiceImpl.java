package com.mobile_app_server.service.impl;

import com.mobile_app_server.dto.ResultSetQuery;
import com.mobile_app_server.dto.ScheduleDto;
import com.mobile_app_server.repo.ScheduleRepo;
import com.mobile_app_server.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepo scheduleRepo;

    @Transactional
    @Override
    public void addSchedule(ScheduleDto scheduleDto) {
        scheduleRepo.insertSchedule(scheduleDto);
    }

    @Override
    @Transactional
    public void deleteSchedule(Integer id) {
        scheduleRepo.deleteSchedule(id);
    }

    @Transactional
    @Override
    public void updateSchedule(ScheduleDto scheduleDto) {
        scheduleRepo.updateSchedule(scheduleDto);
    }

    @Override
    public ScheduleDto getScheduleById(Integer id) {
        return new ScheduleDto(scheduleRepo.getScheduleById(id));
    }

    @Override
    public List<ScheduleDto> getAllSchedule(Integer eventId) {

        List<ScheduleDto> scheduleDtos = new ArrayList<>();

        for (ResultSetQuery query : scheduleRepo.getAllSchedule(eventId)) {
            if (query.getStartdate().equals(query.getEnddate())) {
                scheduleDtos.add(ScheduleDto.builder()
                        .id(query.getId())
                        .name(query.getName())
                        .des(query.getDes())
                        .location(query.getLocation())
                        .startDate(query.getStartdate())
                        .startTime(query.getStarttime())
                        .endDate(query.getEnddate())
                        .endTime(query.getEndtime()).build());
            } else {

                scheduleDtos.add(ScheduleDto.builder()
                        .id(query.getId())
                        .name(query.getName())
                        .des(query.getDes())
                        .location(query.getLocation())
                        .startDate(query.getStartdate())
                        .startTime(query.getStarttime())
                        .endDate(null)
                        .endTime(null).build());

                if (query.getEndtime() != null) {
                    scheduleDtos.add(ScheduleDto.builder()
                            .id(query.getId())
                            .name(query.getName())
                            .des(query.getDes())
                            .location(query.getLocation())
                            .startDate(null)
                            .startTime(null)
                            .endDate(query.getEnddate())
                            .endTime(query.getEndtime()).build());
                }
            }
        }

        return scheduleDtos;
    }
}
