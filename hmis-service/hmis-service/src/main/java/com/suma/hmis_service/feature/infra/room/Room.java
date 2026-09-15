package com.suma.hmis_service.feature.infra.room;

import com.suma.hmis_service.feature.infra.ward.Ward;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "rooms",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_room_code",
                        columnNames = "code"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;
}

