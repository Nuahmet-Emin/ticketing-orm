package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> projectList = projectRepository.findAll();
        return projectList.stream().map(projectMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        //Project dto = projectRepository.findByProjectCode(code);
        return projectMapper.convertToDTO(projectRepository.findByProjectCode(code));
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
       projectRepository.save(projectMapper.convertToEntity(dto));
    }



    @Override
    public void update(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        Project convertedProject = projectMapper.convertToEntity(dto);
        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());
        projectRepository.save(convertedProject);


    }

    @Override
    public void delete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setIsDeleted(true);
        project.setProjectCode(project.getProjectCode() + "_" + project.getId());
        projectRepository.save(project);
        taskService.deleteByProject(projectMapper.convertToDTO(project));

    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        taskService.completeByProject(projectMapper.convertToDTO(project));



    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        //samantha@manager.com
        UserDTO currentUserDTO = userService.findByUserName("samantha@manager.com");
        User user = userMapper.convertToEntity(currentUserDTO);
        List<Project> projectList = projectRepository.findAllByAssignedManager(user);

        return projectList.stream().map(project -> {
            ProjectDTO obj = projectMapper.convertToDTO(project);

            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));

            return obj;
        }).collect(Collectors.toList());

    }
}
