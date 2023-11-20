package com.example.easyevent.service;

import com.example.easyevent.entity.EventEntity;
import com.example.easyevent.entity.UserEntity;
import com.example.easyevent.repository.EventEntityRepository;
import com.example.easyevent.repository.UserEntityRepository;
import com.example.easyevent.type.*;
import com.example.easyevent.util.TokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserEntityService {
    private final UserEntityRepository userEntityRepository;
    private final EventEntityRepository eventEntityRepository;
    private final PasswordEncoder passwordEncoder;
    public User createUser(UserInput userInput) {
        UserEntity user = userEntityRepository.findUserEntityByEmail(userInput.getEmail());
        if (user != null){
            throw new IllegalArgumentException("email already exists");
        }
        userInput.setPassword(passwordEncoder.encode(userInput.getPassword()));
        UserEntity userEntity = UserEntity.fromUserInput(userInput);
        UserEntity save = userEntityRepository.save(userEntity);
        return User.fromEntity(save);
    }

    public List<Event> getEventsByEmail(String email){
        UserEntity currentUser = userEntityRepository.findUserEntityByEmail(email);
        if(currentUser == null){
            throw new IllegalArgumentException("user is not exists");
        }
        List<EventEntity> eventEntitiesByUser = eventEntityRepository.findEventEntitiesByUser(currentUser);
        return eventEntitiesByUser
                .stream()
                .map(Event::fromEventEntity)
                .collect(Collectors.toList());
    }

    public AuthData verifyUser(LoginInput loginInput) {
        UserEntity userEntity = userEntityRepository.findUserEntityByEmail(loginInput.getEmail());
        if(userEntity == null){
            throw  new IllegalArgumentException("no user exists");
        }
        boolean success = passwordEncoder.matches(loginInput.getPassword(), userEntity.getPassword());
        if(!success){
            throw new IllegalArgumentException("password error");
        }
        String token = TokenUtil.signToken(userEntity.getEmail(), 1);
        return AuthData.builder()
                .token(token)
                .userEmail(userEntity.getEmail())
                .tokenExpiration(1)
                .build();
    }
}
