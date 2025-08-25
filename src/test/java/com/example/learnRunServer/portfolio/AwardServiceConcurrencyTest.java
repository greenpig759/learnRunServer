package com.example.learnRunServer.portfolio;

import com.example.learnRunServer.portfolio.DTO.AwardDTO;
import com.example.learnRunServer.portfolio.Repository.AwardRepository;
import com.example.learnRunServer.portfolio.Service.AwardService;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDate;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

// 동시성 테스트
@SpringBootTest
public class AwardServiceConcurrencyTest {

    @Autowired
    private AwardService awardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AwardRepository awardRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        // 테스트 실행 전 사용자 데이터 정리 및 생성
        userRepository.deleteAll();
        testUser = userRepository.saveAndFlush(UserEntity.builder().kakaoId("concurrent_user").build());
    }

    @AfterEach
    void tearDown() {
        // 테스트 실행 후 데이터 정리
        awardRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("두 사용자가 동시에 Award 수정을 시도할 때, 한 명만 성공하고 다른 한 명은 실패해야 한다")
    void testConcurrentUpdate_shouldThrowOptimisticLockException() throws InterruptedException {
        // 1. 테스트용 Award 생성
        AwardDTO initialAward = awardService.toDTO(awardRepository.saveAndFlush(new com.example.learnRunServer.portfolio.Entity.AwardEntity(null, 0L, "Initial Award", LocalDate.now(), testUser)));
        final Long awardId = initialAward.getAwardId();
        final Long initialVersion = initialAward.getVersion();

        // 2. 동시성 테스트 준비
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        // 3. 작업 1: 첫 번째 수정 요청 (성공 예상)
        Future<Exception> future1 = executorService.submit(() -> {
            try {
                awardService.updateAward(awardId, new AwardDTO(awardId, "Update by User 1", LocalDate.now(), initialVersion), testUser.getUserId());
                return null;
            } catch (Exception e) {
                return e;
            } finally {
                latch.countDown();
            }
        });

        // 4. 작업 2: 두 번째 수정 요청 (실패 예상)
        Future<Exception> future2 = executorService.submit(() -> {
            try {
                awardService.updateAward(awardId, new AwardDTO(awardId, "Update by User 2", LocalDate.now(), initialVersion), testUser.getUserId());
                return null;
            } catch (Exception e) {
                return e;
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        // 5. 결과 확인
        try {
            Exception result1 = future1.get();
            Exception result2 = future2.get();

            assertTrue(result1 == null ^ result2 == null, "Exactly one update should succeed and one should fail.");

            Exception thrownException = result1 != null ? result1 : result2;
            assertInstanceOf(ObjectOptimisticLockingFailureException.class, thrownException, "The thrown exception should be ObjectOptimisticLockingFailureException.");

        } catch (Exception e) {
            fail("Test failed with an unexpected exception: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }

        // 6. 최종 데이터베이스 상태 확인
        com.example.learnRunServer.portfolio.Entity.AwardEntity finalAward = awardRepository.findById(awardId).orElseThrow();
        assertEquals(initialVersion + 1, finalAward.getVersion(), "Version should be incremented by 1.");
        assertNotEquals("Initial Award", finalAward.getTitle());
    }
}
