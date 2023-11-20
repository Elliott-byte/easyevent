package com.example.easyevent.fetcher;

import com.example.easyevent.service.UserEntityService;
import com.example.easyevent.type.*;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@DgsComponent
@Slf4j
@AllArgsConstructor
public class UserDataFetcher {
    private final UserEntityService userEntityService;

    @DgsQuery
    public AuthData login(@InputArgument LoginInput loginInput){
        return userEntityService.verifyUser(loginInput);
    }

    @DgsMutation
    public User createUser(@InputArgument UserInput userInput){
        return userEntityService.createUser(userInput);
    }

    @DgsData(parentType = "User", field = "createdEvents")
    public List<Event> createdEvents(DgsDataFetchingEnvironment dfe){
        User user = dfe.getSource();
        return userEntityService.getEventsByEmail(user.getEmail());
    }

}
