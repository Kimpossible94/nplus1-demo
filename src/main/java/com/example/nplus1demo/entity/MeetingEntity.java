package com.example.nplus1demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MeetingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingName;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParticipantEntity> participants = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScheduleEntity> schedules = new ArrayList<>();

    public void addParticipant(ParticipantEntity participantEntity) {
        this.participants.add(participantEntity);
        participantEntity.setMeeting(this);
    }

    public void addSchedule(ScheduleEntity scheduleEntity) {
        this.schedules.add(scheduleEntity);
        scheduleEntity.setMeeting(this);
    }
}
