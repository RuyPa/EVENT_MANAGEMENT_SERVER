package com.mobile_app_server.repo;

import com.mobile_app_server.dto.EventDto;
import com.mobile_app_server.dto.ResultSetQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<EventDto, Integer> {

    @Query(value = "select ev.*, cate.name as cateName, us.name as hostname " +
            " from tblEvent ev " +
            " left join tblEventCategory ec on ec.eventId = ev.id " +
            " left join tblUser us on us.id = ev.userId " +
            " left join tblCategory cate on cate.id = ec.categoryId " +
            " where ev.id = :eventId ", nativeQuery = true)
    List<ResultSetQuery> getEventById(@Param("eventId") Integer eventId);

    @Modifying
    @Query(value =  "INSERT INTO tblEvent (id, userId, name, starttime, endtime, " +
            " location, address, city, des, eventvideo, registrationtype, " +
            " websitelink, imgurl, startdate, enddate ) "
            + "VALUES (:#{#event.id}, :#{#event.userDto.id}, :#{#event.name}, " +
            " :#{#event.startTime}, :#{#event.endTime}, :#{#event.location}, " +
            " :#{#event.address}, :#{#event.city}, :#{#event.des}, " +
            " :#{#event.eventVideo}, :#{#event.registrationType}, " +
            " :#{#event.websiteLink}, :#{#event.imgUrl}, :#{#event.startDate}, :#{#event.endDate})", nativeQuery = true)
    void insertAccessory(@Param("event") EventDto event);
}
