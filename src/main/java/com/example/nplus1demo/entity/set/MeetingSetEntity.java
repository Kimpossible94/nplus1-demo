package com.example.nplus1demo.entity.set;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MeetingSetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingName;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ParticipantSetEntity> participants = new HashSet<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ScheduleSetEntity> schedules = new HashSet<>();

    public void addParticipant(ParticipantSetEntity participantSetEntity) {
        this.participants.add(participantSetEntity);
        participantSetEntity.setMeeting(this);
    }

    public void addSchedule(ScheduleSetEntity scheduleSetEntity) {
        this.schedules.add(scheduleSetEntity);
        scheduleSetEntity.setMeeting(this);
    }
}
