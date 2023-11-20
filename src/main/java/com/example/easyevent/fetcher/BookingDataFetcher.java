package com.example.easyevent.fetcher;

import com.example.easyevent.custom.AuthContext;
import com.example.easyevent.entity.UserEntity;
import com.example.easyevent.service.BookingEntityService;
import com.example.easyevent.type.Booking;
import com.example.easyevent.type.Event;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@DgsComponent
@Slf4j
@AllArgsConstructor
public class BookingDataFetcher {

    BookingEntityService bookingEntityService;

    @DgsMutation
    public Booking bookEvent(@InputArgument String eventId, DataFetchingEnvironment dfe){
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();

        UserEntity userEntity = authContext.getUserEntity();
        return bookingEntityService.saveBooking(Long.valueOf(eventId), userEntity);
    }

    @DgsMutation
    public Event cancelBooking(@InputArgument String bookingId, DataFetchingEnvironment dfe){
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();
        UserEntity userEntity = authContext.getUserEntity();
        return bookingEntityService.cancelBooking(Long.valueOf(bookingId), userEntity);
    }

    @DgsQuery
    public List<Booking> bookings(DataFetchingEnvironment dfe){
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();
        UserEntity userEntity = authContext.getUserEntity();
        return bookingEntityService.findAllByUser(userEntity);
    }
}
