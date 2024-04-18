package com.mobile_app_server.service.impl;

import com.mobile_app_server.dto.ScheduleDto;
import com.mobile_app_server.repo.ScheduleRepo;
import com.mobile_app_server.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        return scheduleRepo.getAllSchedule(eventId).stream()
                .map(ScheduleDto::new)
                .collect(Collectors.toList());
    }
}
