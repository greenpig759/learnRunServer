package com.example.learnRunServer.token.Service;

import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.token.CustomUserDetails;
import com.example.learnRunServer.user.Entity.UserEntity;

import com.example.learnRunServer.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // JPA Repository

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // DB에서 userId를 활용하여 해당 사용자를 조회 -> CustomUser로 변환하여 리턴
        UserEntity userEntity = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found with kakaoId: " + userId));
        return new CustomUserDetails(userEntity);
    }
}
