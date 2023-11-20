package com.example.easyevent.repository;

import com.example.easyevent.entity.BookingEntity;
import com.example.easyevent.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingEntityRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findBookingEntitiesByUserEntity(UserEntity userEntity);
}
