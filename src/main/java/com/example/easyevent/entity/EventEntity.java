package com.example.easyevent.entity;

import com.example.easyevent.type.EventInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "tb_event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity user;

    public String getDate() {
        if (this.date != null) {
            return this.date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return null;
    }

    public void setDate(String dateTimeString) {
        if (dateTimeString != null && !dateTimeString.isEmpty()) {
            try {
                this.date = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid datetime format. Expected format is YYYY-MM-DDTHH:MM:SS.");
            }
        } else {
            this.date = null;
        }
    }

    public static EventEntity fromEventInput(EventInput eventInput, UserEntity user) {

        return EventEntity.builder()
                .title(eventInput.getTitle())
                .description(eventInput.getDescription())
                .price(eventInput.getPrice())
                .date(LocalDateTime.parse(eventInput.getDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .user(user)
                .build();
    }

}
