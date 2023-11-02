package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;

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
}
