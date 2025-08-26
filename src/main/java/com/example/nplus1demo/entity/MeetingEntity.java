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

    public void addParticipant(ParticipantEntity participantEntity) {
        this.participants.add(participantEntity);
        participantEntity.setMeeting(this);
    }
}
