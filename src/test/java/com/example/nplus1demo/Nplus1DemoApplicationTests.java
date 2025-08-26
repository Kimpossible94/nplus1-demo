package com.example.nplus1demo;

import com.example.nplus1demo.entity.MeetingEntity;
import com.example.nplus1demo.entity.ParticipantEntity;
import com.example.nplus1demo.repository.MeetingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Nplus1DemoApplicationTests {

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    @Transactional
    void saveData() {
        MeetingEntity meeting = MeetingEntity.builder()
                .meetingName("스터디 모임")
                .build();

        ParticipantEntity p1 = ParticipantEntity.builder().name("영희").build();
        ParticipantEntity p2 = ParticipantEntity.builder().name("철수").build();
        ParticipantEntity p3 = ParticipantEntity.builder().name("길동").build();
        ParticipantEntity p4 = ParticipantEntity.builder().name("영범").build();
        ParticipantEntity p5 = ParticipantEntity.builder().name("민수").build();

        meeting.addParticipant(p1);
        meeting.addParticipant(p2);
        meeting.addParticipant(p3);
        meeting.addParticipant(p4);
        meeting.addParticipant(p5);

        meetingRepository.save(meeting);
    }

    @Test
    @Transactional
    void nPlus1Test() {
        List<MeetingEntity> meetingEntities = meetingRepository.findAll();
        System.out.println("Meeting Cnt : " + meetingEntities.size());

        for (MeetingEntity meeting : meetingEntities) {
            List<ParticipantEntity> participants = meeting.getParticipants();
            int size = participants.size();
            System.out.println("=================================");
        }
    }
}
