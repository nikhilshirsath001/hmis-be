package com.suma.hmis_service.feature.infra;


import com.suma.hmis_service.feature.infra.bed.Bed;
import com.suma.hmis_service.feature.infra.bed.BedRepository;
import com.suma.hmis_service.feature.infra.bed.BedStatus;
import com.suma.hmis_service.feature.infra.room.Room;
import com.suma.hmis_service.feature.infra.room.RoomRepository;
import com.suma.hmis_service.feature.infra.ward.Ward;
import com.suma.hmis_service.feature.infra.ward.WardRepository;
import com.suma.hmis_service.feature.infra.wardtype.WardType;
import com.suma.hmis_service.feature.infra.wardtype.WardTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WardRoomBedDataInitializer implements CommandLineRunner {

    private final WardTypeRepository wardTypeRepository;
    private final WardRepository wardRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;

    @Override
    public void run(String... args) {

        if (wardTypeRepository.count() > 0) {
            return;
        }

        WardType generalWardType = createWardType(
                "GENERAL",
                "General Ward",
                "General patient ward"
        );

        WardType icuWardType = createWardType(
                "ICU",
                "Intensive Care Unit",
                "Critical care ward"
        );

        Ward generalWard = createWard(
                generalWardType,
                "GEN",
                "General Ward",
                "General patient ward"
        );

        Ward icuWard = createWard(
                icuWardType,
                "ICU",
                "Intensive Care Unit",
                "Critical care ward"
        );

        Room generalRoom1 = createRoom(
                generalWard,
                "GEN-R01",
                "General Room 1",
                "General ward room 1"
        );

        Room generalRoom2 = createRoom(
                generalWard,
                "GEN-R02",
                "General Room 2",
                "General ward room 2"
        );

        Room icuRoom1 = createRoom(
                icuWard,
                "ICU-R01",
                "ICU Room 1",
                "Intensive care unit room 1"
        );

        Room icuRoom2 = createRoom(
                icuWard,
                "ICU-R02",
                "ICU Room 2",
                "Intensive care unit room 2"
        );

        createBeds(generalRoom1, "GEN-R01");
        createBeds(generalRoom2, "GEN-R02");
        createBeds(icuRoom1, "ICU-R01");
        createBeds(icuRoom2, "ICU-R02");

        log.info("Ward, room and bed dummy data initialized");
    }

    private WardType createWardType(
            String code,
            String name,
            String description) {

        WardType wardType = new WardType();
        wardType.setCode(code);
        wardType.setName(name);
        wardType.setDescription(description);
        wardType.setActive(true);
        try {
            return wardTypeRepository.save(wardType);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    private Ward createWard(
            WardType wardType,
            String code,
            String name,
            String description) {

        Ward ward = new Ward();
        ward.setWardType(wardType);
        ward.setCode(code);
        ward.setName(name);
        ward.setDescription(description);
        ward.setActive(true);
        try {
            return wardRepository.save(ward);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    private Room createRoom(
            Ward ward,
            String code,
            String name,
            String description) {

        Room room = new Room();
        room.setWard(ward);
        room.setCode(code);
        room.setName(name);
        room.setDescription(description);
        room.setActive(true);
        try {
            return roomRepository.save(room);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    private void createBeds(
            Room room,
            String roomCode) {

        for (int i = 1; i <= 10; i++) {
            String bedNumber = String.format("%02d", i);

            Bed bed = new Bed();
            bed.setRoom(room);
            bed.setCode(roomCode + "-B" + bedNumber);
            bed.setName("Bed " + i);
            bed.setStatus(BedStatus.AVAILABLE);
            bed.setActive(true);
            try {
                bedRepository.save(bed);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }
}

