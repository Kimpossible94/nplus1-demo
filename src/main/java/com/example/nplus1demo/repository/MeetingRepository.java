package com.example.nplus1demo.repository;

import com.example.nplus1demo.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {

}
