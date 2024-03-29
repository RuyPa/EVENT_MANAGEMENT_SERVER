package com.mobile_app_server.repo;

import com.mobile_app_server.dto.EventCategoryDto;
import com.mobile_app_server.dto.EventDto;
import com.mobile_app_server.dto.ResultSetQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventCateRepo extends JpaRepository<EventCategoryDto, Integer> {

    @Modifying
    @Query(value = "INSERT INTO tblEventCategory (eventId, categoryId) "
            + " VALUES ( :eventId, :cateId) ", nativeQuery = true)
    void insertEventCate(@Param("eventId") Integer eventId,
                         @Param("cateId") Integer cateId);

    @Modifying
    @Query(value = "DELETE FROM tblEventCategory " +
            " WHERE tblEventCategory.eventId = :id", nativeQuery = true)
    void deleteEventCateByEventId(@Param("id") int id);

    @Query(value = " select eventcate.categoryId as id " +
            " from tblEventCategory eventcate " +
            " where eventcate.eventId = :id ", nativeQuery = true)
    List<ResultSetQuery> getEventCateByEventId(@Param("id") Integer id);

    @Modifying
    @Query(value =  "DELETE FROM tblEventCategory " +
            " WHERE tblEventCategory.categoryId = :cateBeforeId", nativeQuery = true)
    void deleteEventCateByEventCateId(@Param("cateBeforeId") Integer cateBeforeId);

}
