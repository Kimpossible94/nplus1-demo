package com.example.nplus1demo.repository.base;

import com.example.nplus1demo.entity.base.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
    @Query("select m from MeetingEntity m join fetch m.participants")
    List<MeetingEntity> findAllWithParticipantsUsingJoinFetch();

    @Query("select m from MeetingEntity m left join fetch m.participants")
    List<MeetingEntity> findAllWithParticipantsUsingLeftJoinFetch();

    @Query("select m from MeetingEntity m " +
            "left join fetch m.participants " +
            "left join fetch m.schedules")
    List<MeetingEntity> findAllWithParticipantsAndSchedules();

    @Query("select m from MeetingEntity m " +
            "left join fetch m.participants " +
            "left join m.schedules")
    List<MeetingEntity> findAllWithParticipantsAndSchedulesOneSideJoinFetch();
}
