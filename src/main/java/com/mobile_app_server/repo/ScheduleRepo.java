package com.mobile_app_server.repo;

import com.mobile_app_server.dto.ResultSetQuery;
import com.mobile_app_server.dto.ScheduleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepo extends JpaRepository<ScheduleDto, Integer> {

    @Modifying
    @Query(value = "INSERT INTO tblschedule (name, startTime, endTime, startDate, endDate, des,location, eventId) " +
            " VALUES ( :#{#dto.name}, :#{#dto.startTime}, " +
            " :#{#dto.endTime}, :#{#dto.startDate}, :#{#dto.endDate}, " +
            ":#{#dto.des}, :#{#dto.location},:#{#dto.eventDto.id} )  ", nativeQuery = true)
    void insertSchedule(@Param("dto") ScheduleDto dto);

    @Modifying
    @Query(value = "DELETE FROM tblschedule WHERE tblschedule.id = :id", nativeQuery = true)
    void deleteSchedule(@Param("id") Integer id);

    @Modifying
    @Query(value = "UPDATE tblschedule " +
            " SET name = :#{#dto.name}, " +
            " startTime = :#{#dto.startTime}, " +
            " startDate = :#{#dto.startDate}, " +
            " endTime = :#{#dto.endTime}, " +
            " endDate = :#{#dto.endDate}, " +
            " des = :#{#dto.des}, " +
            " location = :#{#dto.location} " +
            " where tblschedule.id = :#{#dto.id}", nativeQuery = true)
    void updateSchedule(@Param("dto") ScheduleDto dto);

    @Query(value = "select s.* " +
            " from tblschedule s " +
            " where s.eventId = :eventId",nativeQuery = true)
    List<ResultSetQuery> getAllSchedule(@Param("eventId") Integer eventId);

    @Query(value = "select s.* " +
            " from tblschedule s " +
            " where s.id = :id " +
            " order by s.startDate ", nativeQuery = true)
    ResultSetQuery getScheduleById(@Param("id") Integer id);
}
