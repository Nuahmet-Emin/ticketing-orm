package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
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
        projectRepository.save(project);

    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);



    }
}
