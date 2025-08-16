package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.portfolio.DTO.SkilDTO;
import com.example.learnRunServer.portfolio.Entity.SkilEntity;
import com.example.learnRunServer.portfolio.Repository.SkilRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkilService {
    private final UserRepository userRepository;
    private final SkilRepository skilRepository;

    // DTO -> Entity 변환 메서드
    public SkilEntity toEntity(SkilDTO skilDTO) {
        SkilEntity skilEntity = SkilEntity.builder()
                .title(skilDTO.getTitle())
                .text(skilDTO.getText())
                .build();
        return skilEntity;
    }

    // 새로운 스킬 등록 메서드
    public void saveSkil(SkilDTO skilDTO, Long userId) {
        SkilEntity skilEntity = toEntity(skilDTO);

        // 유저 정보 찾아서 넣기
        UserEntity user = userRepository.findById(userId).orElse(null);
        skilEntity.setUser(user);
        skilRepository.save(skilEntity);
    }


    // 스킬 수정 메서드
    public void updateSkil(SkilDTO skilDTO) {
        SkilEntity skilEntity = skilRepository.findById(skilDTO.getSkilId()).orElse(null);
        skilEntity.setTitle(skilDTO.getTitle());
        skilEntity.setText(skilDTO.getText());
    }

    // 스킬 삭제 메서드
    public void deleteSkil(SkilDTO skilDTO) {
        SkilEntity skilEntity = skilRepository.findById(skilDTO.getSkilId()).orElse(null);

        skilRepository.delete(skilEntity);
    }

    // 스킬 전부 가져오기 메서드
    public List<SkilDTO> findAllSkils(Long userId) {
        List<SkilEntity> skilEntityList = skilRepository.findAllByUser_UserId(userId);

        List<SkilDTO> skilDTOList = new ArrayList<>();
        for(SkilEntity skilEntity : skilEntityList) {
            SkilDTO skilDTO = SkilDTO.builder()
                    .title(skilEntity.getTitle())
                    .text(skilEntity.getText())
                    .build();
            skilDTOList.add(skilDTO);
            skilDTOList.add(skilDTO);
        }
        return skilDTOList;
    }
}
