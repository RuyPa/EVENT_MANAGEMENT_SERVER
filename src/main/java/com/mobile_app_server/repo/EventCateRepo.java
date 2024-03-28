package com.mobile_app_server.repo;

import com.mobile_app_server.dto.EventCategoryDto;
import com.mobile_app_server.dto.EventDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventCateRepo extends JpaRepository<EventCategoryDto, Integer> {

    @Modifying
    @Query(value = "INSERT INTO tblEventCategory (eventId, categoryId) "
            + " VALUES ( :eventId, :cateId) ", nativeQuery = true)
    void insertEventCate(@Param("eventId") Integer eventId,
                         @Param("cateId") Integer cateId);
}
