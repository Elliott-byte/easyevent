package com.example.easyevent.type;

import com.example.easyevent.entity.BookingEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Booking {
    private Integer id;
    private User user;
    private Event event;
    private String createdAt;
    private String updatedAt;

    public static Booking fromEntity(BookingEntity bookingEntiry){
        return Booking.builder()
                .id(bookingEntiry.getId().intValue())
                .user(User.fromEntity(bookingEntiry.getUserEntity()))
                .event(Event.fromEventEntity(bookingEntiry.getEventEntity()))
                .createdAt(bookingEntiry.getCreateAt().toString())
                .updatedAt(bookingEntiry.getUpdatedAt().toString())
                .build();
    }
}
