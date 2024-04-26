package com.mobile_app_server.service;

import com.mobile_app_server.dto.TaskDto;
import org.springframework.http.HttpStatusCode;

import java.util.List;

public interface TaskService {

    void addTask(TaskDto taskDto);

    void deleteTask(Integer id);

    void updateTask(TaskDto taskDto);

    TaskDto getTaskById(Integer id);

    List<TaskDto> getAllTask(Integer eventId);

    void updateChecked(TaskDto taskDto);
}
