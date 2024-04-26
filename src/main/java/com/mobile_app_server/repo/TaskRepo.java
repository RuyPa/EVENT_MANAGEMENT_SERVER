package com.mobile_app_server.repo;

import com.mobile_app_server.dto.ResultSetQuery;
import com.mobile_app_server.dto.TaskDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepo extends JpaRepository<TaskDto, Integer> {

    @Modifying
    @Query(value = "INSERT INTO tbltask (name, startTime, startDate, des, eventId, checked) " +
            " VALUES ( :#{#dto.name}, :#{#dto.startTime}, " +
            " :#{#dto.startDate}, " +
            ":#{#dto.des},:#{#dto.eventDto.id}, 0 )  ", nativeQuery = true)
    void insertTask(@Param("dto") TaskDto dto);

    @Modifying
    @Query(value = "DELETE FROM tbltask WHERE tbltask.id = :id", nativeQuery = true)
    void deleteTask(@Param("id") Integer id);

    @Modifying
    @Query(value = "UPDATE tbltask " +
            " SET name = :#{#dto.name}, " +
            " startTime = :#{#dto.startTime}, " +
            " startDate = :#{#dto.startDate}, " +
            " des = :#{#dto.des} " +
            " where tbltask.id = :#{#dto.id}", nativeQuery = true)
    void updateTask(@Param("dto") TaskDto dto);

    @Query(value = "select s.* " +
            " from tbltask s " +
            " where s.eventId = :eventId", nativeQuery = true)
    List<ResultSetQuery> getAllTask(@Param("eventId") Integer eventId);

    @Query(value = "select s.* " +
            " from tbltask s " +
            " where s.id = :id " +
            " order by s.startDate ", nativeQuery = true)
    ResultSetQuery getTaskById(@Param("id") Integer id);

    @Modifying
    @Query(value = "UPDATE tbltask " +
            "SET checked = CASE WHEN :#{#dto.checked} = 1 THEN 1 ELSE 0 END " +
            "WHERE tbltask.id = :#{#dto.id} ", nativeQuery = true)
    void updateCheckedById(@Param("dto") TaskDto dto);
}
