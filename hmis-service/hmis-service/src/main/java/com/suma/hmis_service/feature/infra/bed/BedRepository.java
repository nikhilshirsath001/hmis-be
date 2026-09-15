package com.suma.hmis_service.feature.infra.bed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {

    boolean existsByCodeIgnoreCase(String code);

    @Query("""
            select b
            from Bed b
            join fetch b.room r
            join fetch r.ward w
            where r.id = :roomId
            and b.active = true
            """)
    List<Bed> findByRoomIdAndActiveTrue(@Param("roomId") Long roomId);

    List<Bed> findByRoomIdAndStatusAndActiveTrue(Long roomId, BedStatus status);

    @Query("""
    select b
    from Bed b
    join fetch b.room r
    join fetch r.ward w
    where b.status = 'AVAILABLE'
      and b.active = true
    """)
    List<Bed> findByAvailableBed(Pageable pageable);
}

