package com.mobile_app_server.service.impl;

import com.mobile_app_server.dto.TaskDto;
import com.mobile_app_server.repo.TaskRepo;
import com.mobile_app_server.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    @Transactional
    @Override
    public void addTask(TaskDto taskDto) {
        taskRepo.insertTask(taskDto);
    }

    @Transactional
    @Override
    public void deleteTask(Integer id) {
        taskRepo.deleteTask(id);
    }

    @Transactional

    @Override
    public void updateTask(TaskDto taskDto) {
        taskRepo.updateTask(taskDto);
    }

    @Override
    public TaskDto getTaskById(Integer id) {
        return new TaskDto(taskRepo.getTaskById(id));
    }

    @Override
    public List<TaskDto> getAllTask(Integer eventId) {
        return taskRepo.getAllTask(eventId).stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateChecked(TaskDto taskDto) {
        taskRepo.updateCheckedById(taskDto);
    }
}
