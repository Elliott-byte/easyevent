package com.example.easyevent.custom;

import com.example.easyevent.entity.UserEntity;
import lombok.Data;

@Data
public class AuthContext {
    private UserEntity userEntity;
    private boolean isValidToken;

    public void ensureAuthenticated(){
        if(!isValidToken) throw new IllegalArgumentException("not valid");
        if(userEntity == null) throw new IllegalArgumentException("please login");
    }
}
