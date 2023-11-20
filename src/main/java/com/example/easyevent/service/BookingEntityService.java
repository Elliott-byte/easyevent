package com.example.easyevent.service;

import com.example.easyevent.entity.BookingEntity;
import com.example.easyevent.entity.EventEntity;
import com.example.easyevent.entity.UserEntity;
import com.example.easyevent.repository.BookingEntityRepository;
import com.example.easyevent.repository.EventEntityRepository;
import com.example.easyevent.type.Booking;
import com.example.easyevent.type.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingEntityService {

    BookingEntityRepository bookingEntityRepository;
    EventEntityRepository eventEntityRepository;
    public Booking saveBooking(Long eventId, UserEntity userEntity) {
        EventEntity eventEntity = eventEntityRepository.findById(eventId).orElse(null);
        if (eventEntity == null){
            throw new IllegalArgumentException("event not exits");
        }
        log.info("user id is: "+ userEntity.getId());
        BookingEntity bookingEntity = BookingEntity.builder()
                .eventEntity(eventEntity)
                .userEntity(userEntity)
                .createAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        BookingEntity save = bookingEntityRepository.save(bookingEntity);
        return Booking.fromEntity(save);
    }

    public Event cancelBooking(Long bookingId, UserEntity userEntity) {
        BookingEntity bookingEntity = bookingEntityRepository.findById(bookingId).orElse(null);
        if (bookingEntity == null){
            throw new IllegalArgumentException("event not exits");
        }
        if (!bookingEntity.getUserEntity().getId().equals(userEntity.getId())){
            log.info(bookingEntity.getUserEntity().getId().toString());
            log.info(userEntity.getId().toString());
            throw new IllegalArgumentException("not allowed");
        }
        EventEntity eventEntity = eventEntityRepository.findById(bookingEntity.getEventEntity().getId()).orElse(null);
        bookingEntityRepository.deleteById(bookingId);
        assert eventEntity != null;
        return Event.fromEventEntity(eventEntity);
    }

    public List<Booking> findAllByUser(UserEntity userEntity) {
        List<BookingEntity> all = bookingEntityRepository.findBookingEntitiesByUserEntity(userEntity);
        return all.stream().map(Booking::fromEntity).collect(Collectors.toList());
    }
}
