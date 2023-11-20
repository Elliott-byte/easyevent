package com.example.easyevent.custom;

import com.example.easyevent.entity.UserEntity;
import com.example.easyevent.repository.UserEntityRepository;
import com.example.easyevent.util.TokenUtil;
import com.netflix.graphql.dgs.context.DgsCustomContextBuilderWithRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class AuthContextBuilder implements DgsCustomContextBuilderWithRequest {
    private final UserEntityRepository userEntityRepository;

    static final String AUTHORIZATION_HEADER ="Authorization";

    @Override
    public Object build(@Nullable Map map, @Nullable HttpHeaders httpHeaders, @Nullable WebRequest webRequest) {
        log.info("Building Auth Context...");
        AuthContext authContext = new AuthContext();
        if (!httpHeaders.containsKey(AUTHORIZATION_HEADER)){
            log.info("user is not authed");
            return authContext;
        }
        String authorization = httpHeaders.getFirst(AUTHORIZATION_HEADER);
        String token = authorization.replace("Bearer ", "");
        String userEmail = "";
        try {
            userEmail = TokenUtil.verifyToken(token);
        }catch (Exception e){
            authContext.setValidToken(false);
        }
        UserEntity currUser = userEntityRepository.findUserEntityByEmail(userEmail);
        if (currUser == null){
            authContext.setValidToken(false);
            return authContext;
        }
        authContext.setUserEntity(currUser);
        authContext.setValidToken(true);
        log.info("user login success = {}", userEmail);

        return authContext;
    }
}
