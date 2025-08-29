package com.example.nplus1demo;

import com.example.nplus1demo.entity.base.MeetingEntity;
import com.example.nplus1demo.entity.base.ParticipantEntity;
import com.example.nplus1demo.entity.base.ScheduleEntity;
import com.example.nplus1demo.entity.set.MeetingSetEntity;
import com.example.nplus1demo.entity.set.ParticipantSetEntity;
import com.example.nplus1demo.entity.set.ScheduleSetEntity;
import com.example.nplus1demo.repository.base.MeetingRepository;
import com.example.nplus1demo.repository.set.MeetingSetRepository;
import jakarta.transaction.Transactional;
import org.hibernate.loader.MultipleBagFetchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FetchJoinTests {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingSetRepository meetingSetRepository;

    void saveMeetingData() {
        MeetingEntity meeting = MeetingEntity.builder()
                .meetingName("스터디 모임")
                .build();

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

    void saveMeetingDataWithParticipantAndSchedulesV2() {
        MeetingSetEntity meeting = MeetingSetEntity.builder()
                .meetingName("스터디 모임")
                .build();

        ParticipantSetEntity p1 = ParticipantSetEntity.builder().name("영희").build();
        ParticipantSetEntity p2 = ParticipantSetEntity.builder().name("철수").build();
        ParticipantSetEntity p3 = ParticipantSetEntity.builder().name("길동").build();
        ParticipantSetEntity p4 = ParticipantSetEntity.builder().name("영범").build();
        ParticipantSetEntity p5 = ParticipantSetEntity.builder().name("민수").build();

        meeting.addParticipant(p1);
        meeting.addParticipant(p2);
        meeting.addParticipant(p3);
        meeting.addParticipant(p4);
        meeting.addParticipant(p5);

        ScheduleSetEntity s1 = ScheduleSetEntity.builder().name("1번 스케쥴").build();
        ScheduleSetEntity s2 = ScheduleSetEntity.builder().name("2번 스케쥴").build();
        ScheduleSetEntity s3 = ScheduleSetEntity.builder().name("3번 스케쥴").build();
        ScheduleSetEntity s4 = ScheduleSetEntity.builder().name("4번 스케쥴").build();

        meeting.addSchedule(s1);
        meeting.addSchedule(s2);
        meeting.addSchedule(s3);
        meeting.addSchedule(s4);

        meetingSetRepository.save(meeting);
    }

    @Test
    @DisplayName("fetch join 데이터 누락 테스트")
    @Transactional
    void fetchJoinDataMissingTest() {
        saveMeetingDataWithParticipantAndSchedules();
        saveMeetingData();

        List<MeetingEntity> meetingEntities = meetingRepository.findAllWithParticipantsUsingJoinFetch();
        System.out.println("left join fetch 적용 전 : " + meetingEntities.size());

        System.out.println("====================================================");

        List<MeetingEntity> leftMeetingEntities = meetingRepository.findAllWithParticipantsUsingLeftJoinFetch();
        System.out.println("left join fetch 적용 후 : " + leftMeetingEntities.size());
    }

    @Test
    @DisplayName("2개 이상의 OneToMany에서 fetch join 실패 확인")
    @Transactional
    void multipleOneToManyFetchJoinTest() {
        saveMeetingDataWithParticipantAndSchedules();

        assertThatThrownBy(() -> meetingRepository.findAllWithParticipantsAndSchedules())
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .rootCause()
                .isInstanceOf(MultipleBagFetchException.class);
    }

    @Test
    @DisplayName("2개 이상의 OneToMany에서 Set으로 변경후 fetch join 테스트")
    @Transactional
    void multipleOneToManyFetchJoinSetTest() {
        saveMeetingDataWithParticipantAndSchedulesV2();

        List<MeetingSetEntity> allWithParticipantsAndSchedules = meetingSetRepository.findAllWithParticipantsAndSchedules();
        System.out.println("Meeting Cnt: " + allWithParticipantsAndSchedules.size());
    }

    @Test
    @DisplayName("2개 이상의 OneToMany에서 @BatchSize 적용후 fetch join 테스트")
    @Transactional
    void multipleOneToManyOneSideFetchJoinTest() {
        saveMeetingDataWithParticipantAndSchedules();

        List<MeetingEntity> allWithParticipantsAndSchedules = meetingRepository.findAllWithParticipantsAndSchedulesOneSideJoinFetch();
        System.out.println("Meeting Cnt: " + allWithParticipantsAndSchedules.size());

        for (MeetingEntity meetingEntity: allWithParticipantsAndSchedules) {
            meetingEntity.getParticipants().size();
            meetingEntity.getSchedules().size();
        }
    }

    @Test
    @DisplayName("fetch join pageable 테스트")
    @Transactional
    void fetchJoinPageableTest() {
        saveMeetingDataWithParticipantAndSchedules();

        Page<MeetingEntity> all = meetingRepository.findAll(PageRequest.of(0, 1));
        System.out.println(all.get().toList().size());
    }
}
