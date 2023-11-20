package com.example.easyevent.repository;

import com.example.easyevent.entity.EventEntity;
import com.example.easyevent.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findEventEntitiesByUser(UserEntity user);
}
