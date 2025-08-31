package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.ProjectDTO;
import com.example.learnRunServer.portfolio.Service.ProjectService;
import com.example.learnRunServer.token.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/learnRun")
@Tag(name = "Project API", description = "프로젝트 관련 API 모음")
public class ProjectController {
    private final ProjectService projectService;

    // 프로젝트 글 추가 컨트롤러
    @PostMapping("/project")
    @Operation(summary = "프로젝트 등록", description = "새로운 프로젝트를 등록, 생성된 프로젝트 ID를 반납")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "프로젝트 등록 성공")
    })
    public ResponseEntity<Long> saveProject(@RequestBody ProjectDTO projectDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long projectId = projectService.saveProject(projectDTO, customUserDetails.getUserId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(projectId)
                .toUri();
        return ResponseEntity.created(location).body(projectId);
    }

    // 프로젝트 글 수정 컨트롤러
    @PutMapping("/project/{projectId}")
    @Operation(summary = "프로젝트 수정", description = "기존의 프로젝트를 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    public ResponseEntity<Void> updateProject(@RequestBody ProjectDTO projectDTO, @PathVariable Long projectId) {
        projectService.updateProject(projectDTO, projectId);
        return ResponseEntity.ok().build();
    }

    // 프로젝트 글 삭제 컨트롤러
    @DeleteMapping("/project/{projectId}")
    @Operation(summary = "프로젝트 삭제", description = "지정한 프로젝트를 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공")
    })
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.deleteProject(projectId, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    // 프로젝트 글 미리보기 리스트 컨트롤러
    @GetMapping("/project")
    @Operation(summary = "프로젝트 글 리스트 조회", description = "리스트중 하나를 누르면 상세보기를 지원")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리스트 가져오기 성공")
    })
    public ResponseEntity<List<ProjectDTO>> getAllProjects(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ProjectDTO> projectDTOList = projectService.getAllProjects(customUserDetails.getUserId());
        return ResponseEntity.ok().body(projectDTOList);
    }

    // 프로젝트 글 상세보기 컨트롤러
    @GetMapping("/project/{projectId}")
    @Operation(summary = "글 상세보기", description = "해당 글의 전체 내용을 전부 가져온다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상세조회 성공")
    })
    public ResponseEntity<ProjectDTO> getProjectDetail(@PathVariable Long projectId) {
        ProjectDTO project = projectService.getProjectDetail(projectId);
        return ResponseEntity.ok().body(project);
    }
}
