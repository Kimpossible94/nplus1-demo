package com.example.nplus1demo.repository.set;

import com.example.nplus1demo.entity.set.MeetingSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingSetRepository extends JpaRepository<MeetingSetEntity, Long> {
    @Query("select m from MeetingSetEntity m " +
            "left join fetch m.participants " +
            "left join fetch m.schedules")
    List<MeetingSetEntity> findAllWithParticipantsAndSchedules();
}
