package com.suma.hmis_service.feature.infra.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByCodeIgnoreCase(String code);

    List<Room> findByWardIdAndActiveTrue(Long wardId);
}

