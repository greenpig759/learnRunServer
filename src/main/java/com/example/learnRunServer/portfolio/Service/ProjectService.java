package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.portfolio.DTO.ProjectDTO;
import com.example.learnRunServer.portfolio.Entity.ProjectEntity;
import com.example.learnRunServer.portfolio.Repository.ProjectRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // DTO -> Entity 변환 메서드
    public ProjectEntity toEntity(ProjectDTO projectDTO){
        ProjectEntity projectEntity = ProjectEntity.builder()
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .title(projectDTO.getTitle())
                .text(projectDTO.getText())
                .build();

        return projectEntity;
    }

    // 프로젝트 글 추가 메서드
    public void saveProject(ProjectDTO projectDTO, Long userId){
        // 1. DTO를 Entity로 변환
        ProjectEntity projectEntity = toEntity(projectDTO);
        // 2. 유저의 정보를 추가해주기
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        projectEntity.setUser(userEntity);
        projectRepository.save(projectEntity);
    }

    // 프로젝트 글 수정 메서드
    @Transactional
    public void updateProject(ProjectDTO projectDTO){
        // 1. 기존에 있던 Entity 불러오기
        ProjectEntity projectEntity = projectRepository.findByProjectId(projectDTO.getProjectId())
                .orElseThrow(()-> new IllegalArgumentException("해당 글 없음"));

        // 2. 기존의 내용에 새로운 내용 붙여넣고 다시 저장하기
        projectEntity.setStartDate(projectDTO.getStartDate());
        projectEntity.setEndDate(projectDTO.getEndDate());
        projectEntity.setTitle(projectDTO.getTitle());
        projectEntity.setText(projectDTO.getText());
    }

    // 프로젝트 글 삭제 메서드
    public void deleteProject(Long projectId){
        projectRepository.findByProjectId(projectId);
        projectRepository.deleteById(projectId);
    }

    // 프로젝트 글 리스트 불러오기 메서드
    public List<ProjectDTO> getAllProjects(Long userId){
        // 1. 해당 유저의 모든 글정보(제목, 시작날짜, 끝날짜)
        List<ProjectEntity> projectEntityList = projectRepository.findAllByUserId(userId);

        // 3. 해당 Entity에서 제목, 시작날짜, 끝날짜만 DTO로 변환 후 봔환한다
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        for(ProjectEntity projectEntity : projectEntityList){
            ProjectDTO projectDTO = ProjectDTO.builder()
                    .title(projectEntity.getTitle())
                    .startDate(projectEntity.getStartDate())
                    .endDate(projectEntity.getEndDate())
                    .build();
            projectDTOList.add(projectDTO);
        }

        return projectDTOList;
    }

    // 프로젝트 글 상세보기 메서드
    public ProjectDTO getProjectDetail(Long projectId){
        // 1. 해당 프로젝트의 Entity 가져오기
        ProjectEntity projectEntity = projectRepository.findByProjectId(projectId)
                .orElse(null);
        // 2. DTO로 변환하여 리턴하기
        ProjectDTO projectDTO = ProjectDTO.builder()
                .projectId(projectEntity.getProjectId())
                .startDate(projectEntity.getStartDate())
                .endDate(projectEntity.getEndDate())
                .title(projectEntity.getTitle())
                .text(projectEntity.getText())
                .build();

        return projectDTO;
    }
}
