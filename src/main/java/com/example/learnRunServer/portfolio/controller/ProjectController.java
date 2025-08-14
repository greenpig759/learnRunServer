package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.ProjectDTO;
import com.example.learnRunServer.portfolio.Entity.ProjectEntity;
import com.example.learnRunServer.portfolio.Repository.ProjectRepository;
import com.example.learnRunServer.portfolio.Service.ProjectService;
import com.example.learnRunServer.token.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Repository
@RequiredArgsConstructor
@RequestMapping("/learnRun/")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

    // 프로젝트 글 추가 컨트롤러
    @PostMapping("/project/save")
    public ResponseEntity<Void> saveProject(@RequestBody ProjectDTO projectDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.saveProject(projectDTO, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    // 프로젝트 글 수정 컨트롤러
    @PatchMapping("/project/update")
    public ResponseEntity<Void> updateProject(ProjectDTO projectDTO) {
        projectService.updateProject(projectDTO);
        return ResponseEntity.ok().build();
    }

    // 프로젝트 글 삭제 컨트롤러
    @DeleteMapping("/project/delete")
    public ResponseEntity<Void> deleteProject(@RequestBody ProjectDTO projectDTO) {
        // 1. 기본키로 프로젝트의 Entity를 찾기
        ProjectEntity projectEntity = projectRepository.findByProjectId(projectDTO.getProjectId())
                .orElseThrow();

        projectService.deleteProject(projectDTO.getProjectId());
        return ResponseEntity.ok().build();
    }

    // 프로젝트 글 미리보기 리스트 컨트롤러
    @PostMapping("/project/all/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ProjectDTO> projectDTOList = projectService.getAllProjects(customUserDetails.getUserId());
        return ResponseEntity.ok().body(projectDTOList);
    }

    // 프로젝트 글 상세보기 컨트롤러
    @PostMapping("/project/detail/project")
    public ResponseEntity<ProjectDTO> getProjectDetail(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO project = projectService.getProjectDetail(projectDTO.getProjectId());
        return ResponseEntity.ok().body(project);
    }
}
