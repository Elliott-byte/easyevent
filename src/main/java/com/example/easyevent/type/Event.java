package com.example.easyevent.type;

import com.example.easyevent.entity.EventEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private String id;
    private String title;
    private String description;
    private Double price;
    private String date;
    private User creator;

    public static Event fromEventEntity(EventEntity eventEntity){
        return Event.builder()
                .id(eventEntity.getId().toString())
                .title(eventEntity.getTitle())
                .description(eventEntity.getDescription())
                .price(eventEntity.getPrice())
                .date(eventEntity.getDate())
                .creator(User.fromEntity(eventEntity.getUser()))
                .build();
    }
}
