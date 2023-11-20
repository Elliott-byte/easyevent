package com.example.easyevent.entity;

import com.example.easyevent.type.UserInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    public static UserEntity fromUserInput(UserInput userInput){
        return UserEntity.builder()
                .email(userInput.getEmail())
                .password(userInput.getPassword())
                .build();
    }
}
