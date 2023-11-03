package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper  projectMapper;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskDTO> listAllTask() {
        List<Task> taskList = taskRepository.findAll();
        return taskList.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional <Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            return taskMapper.convertToDTO(task.get());
        }
        return null;

    }

    @Override
    public void update(TaskDTO dto) {

        Optional<Task> task = taskRepository.findById(dto.getId());
        Task convertedTask = taskMapper.convertToEntity(dto);
        if(task.isPresent()){
            convertedTask.setId(task.get().getId());
            convertedTask.setTaskStatus(task.get().getTaskStatus()== null? task.get().getTaskStatus() : dto.getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }



    }

    @Override
    public void delete(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            task.get().setIsDeleted(true);
            taskRepository.save(task.get());
        }


    }

    @Override
    public void completeByProject(ProjectDTO dto) {
        List<TaskDTO> list = listAllByProject(dto);
        list.forEach(taskDTO->{
            taskDTO.setTaskStatus(Status.COMPLETE);
            update(taskDTO);
        });
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
       User loggedUser = userRepository.findByUserName("john@employee.com");
       List<Task> list = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,loggedUser);

       return list.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void updateStatus(TaskDTO dto) {
       Optional <Task> task = taskRepository.findById(dto.getId());

        if (task.isPresent()) {
            task.get().setTaskStatus(dto.getTaskStatus());
            taskRepository.save(task.get());
        }
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        User loggedUser = userRepository.findByUserName("john@employee.com");
        List<Task> list = taskRepository.findAllByTaskStatusAndAssignedEmployee(status,loggedUser);

        return list.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteByProject(ProjectDTO dto) {
       List<TaskDTO> list = listAllByProject(dto);
       list.forEach(taskDTO-> delete(taskDTO.getId()));
    }

    public List<TaskDTO> listAllByProject(ProjectDTO dto){
        List<Task> list =taskRepository.findAllByProject(projectMapper.convertToEntity(dto));
        return list.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public void save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        taskRepository.save(taskMapper.convertToEntity(dto));
    }
}


