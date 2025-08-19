package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.SkilNotFoundException;
import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.portfolio.DTO.SkilDTO;
import com.example.learnRunServer.portfolio.Entity.SkilEntity;
import com.example.learnRunServer.portfolio.Repository.SkilRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkilService {
    private final UserRepository userRepository;
    private final SkilRepository skilRepository;

    // DTO -> Entity 변환 메서드
    public SkilEntity toEntity(SkilDTO skilDTO) {
        return SkilEntity.builder()
                .title(skilDTO.getTitle())
                .text(skilDTO.getText())
                .build();
    }

    // 새로운 스킬 등록 메서드
    @Transactional
    public Long saveSkil(SkilDTO skilDTO, Long userId) {
        SkilEntity skilEntity = toEntity(skilDTO);

        // 유저 정보 찾아서 넣기
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        skilEntity.setUser(user);
        skilRepository.save(skilEntity);

        return skilEntity.getSkilId();
    }


    // 스킬 수정 메서드
    @Transactional
    public void updateSkil(Long skilId, SkilDTO skilDTO) {
        SkilEntity skilEntity = skilRepository.findById(skilId)
                .orElseThrow(() -> new SkilNotFoundException("Skil not found with id: " + skilId));
        skilEntity.setTitle(skilDTO.getTitle());
        skilEntity.setText(skilDTO.getText());
    }

    // 스킬 삭제 메서드
    @Transactional
    public void deleteSkil(Long skilId) {
        SkilEntity skilEntity = skilRepository.findById(skilId)
                .orElseThrow(() -> new SkilNotFoundException("Skil not found with id: " + skilId));

        skilRepository.delete(skilEntity);
    }

    // 스킬 전부 가져오기 메서드
    @Transactional(readOnly = true)
    public List<SkilDTO> findAllSkils(Long userId) {
        List<SkilEntity> skilEntityList = skilRepository.findAllByUser_UserId(userId);

        List<SkilDTO> skilDTOList = new ArrayList<>();
        for(SkilEntity skilEntity : skilEntityList) {
            SkilDTO skilDTO = SkilDTO.builder()
                    .skilId(skilEntity.getSkilId())
                    .title(skilEntity.getTitle())
                    .text(skilEntity.getText())
                    .build();
            skilDTOList.add(skilDTO);
        }
        return skilDTOList;
    }
}
