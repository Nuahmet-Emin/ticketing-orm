package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {

    List<TaskDTO> listAllTask();
    TaskDTO findById(Long id);

    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);
   int  totalNonCompletedTask(String projectCode);
   int  totalCompletedTask(String projectCode);

    void deleteByProject(ProjectDTO dto);

    void completeByProject(ProjectDTO dto);

    List<TaskDTO> listAllTasksByStatusIsNot(Status status);

    void updateStatus(TaskDTO dto);

    List<TaskDTO> listAllTasksByStatus(Status status);

    List<TaskDTO> readAllByAssignedEmployee(User assignedEmployee);
}
