package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.portfolio.DTO.SkilDTO;
import com.example.learnRunServer.portfolio.Entity.SkilEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkilService {
    private final UserRepository userRepository;

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

    }


    // 스킬 수정 메서드

    // 스킬 삭제 메서드

    // 스킬 전부 가져오기 메서드
}
