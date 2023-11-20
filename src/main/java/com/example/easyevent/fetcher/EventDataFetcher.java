package com.example.easyevent.fetcher;

import com.example.easyevent.custom.AuthContext;
import com.example.easyevent.entity.EventEntity;
import com.example.easyevent.service.EventEntityService;
import com.example.easyevent.type.Event;
import com.example.easyevent.type.EventInput;
import com.example.easyevent.type.User;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.context.DgsContext;
import lombok.AllArgsConstructor;

import java.util.List;

@DgsComponent
@AllArgsConstructor
public class EventDataFetcher {

    private final EventEntityService entityService;

    @DgsQuery
    public List<Event> events(){
        return entityService.getAllEvents();
    }

    @DgsMutation
    public Event createEvent(@InputArgument(name = "eventInput") EventInput input, DgsDataFetchingEnvironment dfe){
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();
        return entityService.saveOneEvent(input, authContext.getUserEntity());
    }

}
