package com.mobile_app_server.service;

import com.mobile_app_server.dto.ScheduleDto;

import java.util.List;

public interface ScheduleService {

    void addSchedule(ScheduleDto scheduleDto);

    void deleteSchedule(Integer id);

    void updateSchedule(ScheduleDto scheduleDto);

    ScheduleDto getScheduleById(Integer id);

    List<ScheduleDto> getAllSchedule(Integer eventId);
}
