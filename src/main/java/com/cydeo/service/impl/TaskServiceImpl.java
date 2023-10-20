package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public List<TaskDTO> listAllTask() {
        return null;
    }

    @Override
    public TaskDTO findByID(Long id) {
        return null;
    }
}


