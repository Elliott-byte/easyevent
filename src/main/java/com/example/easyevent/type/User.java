package com.example.easyevent.type;

import com.example.easyevent.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    private String email;
    private String password;
    private List<Event> createdEvents;
    public static User fromEntity(UserEntity userEntity){
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .build();
    }
}
