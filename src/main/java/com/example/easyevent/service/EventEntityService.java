package com.example.easyevent.service;

import com.example.easyevent.entity.EventEntity;
import com.example.easyevent.entity.UserEntity;
import com.example.easyevent.repository.EventEntityRepository;
import com.example.easyevent.repository.UserEntityRepository;
import com.example.easyevent.type.Event;
import com.example.easyevent.type.EventInput;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventEntityService {
    private final EventEntityRepository eventEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public List<Event> getAllEvents(){
        List<EventEntity> eventEntities = eventEntityRepository.findAll();
        return eventEntities.stream()
                .map(Event::fromEventEntity)
                .collect(Collectors.toList());
    }

    public Event saveOneEvent(EventInput eventInput, UserEntity userEntity){
        EventEntity event = EventEntity.fromEventInput(eventInput, userEntity);
        EventEntity save = eventEntityRepository.save(event);
        return Event.fromEventEntity(save);
    }
}
