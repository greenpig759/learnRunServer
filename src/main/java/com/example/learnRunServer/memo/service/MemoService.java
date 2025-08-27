package com.example.learnRunServer.memo.service;

import com.example.learnRunServer.memo.DTO.MemoDTO;
import com.example.learnRunServer.memo.Entity.MemoEntity;
import com.example.learnRunServer.memo.repository.MemoRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;

    // DTO -> Entity 변환 메서드
    public MemoEntity toEntity(MemoDTO memoDTO) {
        MemoEntity memoEntity = MemoEntity.builder()
                .Id(memoDTO.getMemoId())
                .memoTitle(memoDTO.getMemoTitle())
                .memoContent(memoDTO.getMemoContent())
                .build();
        return memoEntity;
    }


    // 새로운 글 등록 메서드
    public void saveMemo(MemoDTO memoDTO, Long userId){
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        MemoEntity memoEntity = toEntity(memoDTO);

        memoEntity.setUser(userEntity.get());
        memoRepository.save(memoEntity);
    }

    // 글 수정 저장 메서드
    public void updateMemo(MemoDTO memoDTO){
        MemoEntity memoEntity = memoRepository.findById(memoDTO.getMemoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메모가 존재하지 않습니다"));


        // 정상적으로 찾았다면 내용을 수정한다
        memoEntity.setMemoTitle(memoDTO.getMemoTitle());
        memoEntity.setMemoContent(memoDTO.getMemoContent());
        memoRepository.save(memoEntity);
    }

    // 글 삭제 메서드
    public void deleteMemo(MemoDTO memoDTO){
        MemoEntity memoEntity = memoRepository.findById(memoDTO.getMemoId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 메모가 존재하지 않습니다"));
        memoRepository.delete(memoEntity);
    }

    // 내가 작성한 글 전부 불러오기
    public List<MemoDTO> getAllMemos(Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 없습니다"));

        // 유저를 찾은 뒤 해당 유저를 외래키로 가지는 모든 메모를 가져온다 -> Entity를 DTO로 변환하여 기본키와 제목만 가져옴
        List<MemoEntity> memoEntities = memoRepository.findAllByUser(userEntity);
        List<MemoDTO> memoDTOs = new ArrayList<>();

        // for문을 통해 Entity -> DTO 과정 수행
        for (MemoEntity memoEntity : memoEntities) {
            MemoDTO dto = MemoDTO.builder()
                    .memoId(memoEntity.getId())
                    .memoTitle(memoEntity.getMemoTitle())
                    .build();
            memoDTOs.add(dto);
        }

        return memoDTOs;
    }
}

// 개선점 -> 저장, 삭제를 하는 경우도 유저를 확인하는 것이 맞음, 코드 추가 예정
