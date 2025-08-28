package com.example.nplus1demo;

import com.example.nplus1demo.entity.MeetingEntity;
import com.example.nplus1demo.entity.ParticipantEntity;
import com.example.nplus1demo.entity.ScheduleEntity;
import com.example.nplus1demo.repository.MeetingRepository;
import jakarta.transaction.Transactional;
import org.hibernate.loader.MultipleBagFetchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class Nplus1DemoApplicationTests {

    @Autowired
    private MeetingRepository meetingRepository;

    void saveMeetingData() {
        MeetingEntity meeting = MeetingEntity.builder()
                .meetingName("스터디 모임")
                .build();

        meetingRepository.save(meeting);
    }

    void saveMeetingDataWithParticipant() {
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

    void saveMeetingDataWithParticipantAndSchedules() {
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

        ScheduleEntity s1 = ScheduleEntity.builder().name("1번 스케쥴").build();
        ScheduleEntity s2 = ScheduleEntity.builder().name("2번 스케쥴").build();
        ScheduleEntity s3 = ScheduleEntity.builder().name("3번 스케쥴").build();
        ScheduleEntity s4 = ScheduleEntity.builder().name("4번 스케쥴").build();

        meeting.addSchedule(s1);
        meeting.addSchedule(s2);
        meeting.addSchedule(s3);
        meeting.addSchedule(s4);

        meetingRepository.save(meeting);
    }

    @Test
    @DisplayName("N + 1 테스트")
    @Transactional
    void nPlus1Test() {
        saveMeetingDataWithParticipant();

        List<MeetingEntity> meetingEntities = meetingRepository.findAll();
        System.out.println("Meeting Cnt : " + meetingEntities.size());

        for (MeetingEntity meeting : meetingEntities) {
            List<ParticipantEntity> participants = meeting.getParticipants();
            int size = participants.size();
            System.out.println("=================================");
        }
    }


    @Test
    @DisplayName("fetch join 적용 후 N + 1 테스트")
    @Transactional
    void nPlus1TestUsingQueryAnnotation() {
        saveMeetingDataWithParticipant();

        List<MeetingEntity> meetingEntities = meetingRepository.findAllWithParticipantsUsingJoinFetch();
        System.out.println("Meeting Cnt : " + meetingEntities.size());

        for (MeetingEntity meeting : meetingEntities) {
            List<ParticipantEntity> participants = meeting.getParticipants();
            int size = participants.size();
            System.out.println("=================================");
        }
    }

    @Test
    @DisplayName("fetch join 데이터 누락 테스트")
    @Transactional
    void fetchJoinDataMissingTest() {
        saveMeetingDataWithParticipant();
        saveMeetingData();

        List<MeetingEntity> meetingEntities = meetingRepository.findAllWithParticipantsUsingJoinFetch();
        System.out.println("left join fetch 적용 전 : " + meetingEntities.size());

        System.out.println("====================================================");

        List<MeetingEntity> leftMeetingEntities = meetingRepository.findAllWithParticipantsUsingLeftJoinFetch();
        System.out.println("left join fetch 적용 후 : " + leftMeetingEntities.size());
    }

    @Test
    @DisplayName("2개 이상의 OneToMany에서 fetch join 실패 확인")
    void multipleOneToManyFetchJoinTest() {
        saveMeetingDataWithParticipantAndSchedules();

        assertThatThrownBy(() -> meetingRepository.findAllWithParticipantsAndSchedules())
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .rootCause()
                .isInstanceOf(MultipleBagFetchException.class);
    }


}
