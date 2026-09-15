package com.suma.hmis_service.feature.infra.ward;

import com.suma.hmis_service.entities.Patient;
import com.suma.hmis_service.exceptions.ResourceNotFoundException;
import com.suma.hmis_service.feature.infra.bed.*;
import com.suma.hmis_service.feature.infra.room.RoomRepository;
import com.suma.hmis_service.feature.infra.room.Room;
import com.suma.hmis_service.feature.infra.room.RoomRequest;
import com.suma.hmis_service.feature.infra.room.RoomResponse;
import com.suma.hmis_service.feature.infra.wardtype.WardType;
import com.suma.hmis_service.feature.infra.wardtype.WardTypeRepository;
import com.suma.hmis_service.feature.infra.wardtype.WardTypeRequest;
import com.suma.hmis_service.feature.infra.wardtype.WardTypeResponse;
import com.suma.hmis_service.repositories.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WardServiceImpl implements WardService {

    private static final Logger log = LoggerFactory.getLogger(WardServiceImpl.class);

    private final WardTypeRepository wardTypeRepository;
    private final WardRepository wardRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final ModelMapper modelMapper;
    private final PatientRepository patientRepository;

    @Override
    public WardTypeResponse createWardType(WardTypeRequest request) {
        String code = request.getCode().trim().toUpperCase();
        String name = request.getName().trim();

        if (wardTypeRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Ward type code already exists");
        }

        if (wardTypeRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Ward type name already exists");
        }

        WardType wardType = modelMapper.map(request, WardType.class);
        wardType.setCode(code);
        wardType.setName(name);

        WardType saved = wardTypeRepository.save(wardType);

        log.info(
                "Ward type created successfully with id={} code={}",
                saved.getId(),
                saved.getCode()
        );

        return modelMapper.map(saved, WardTypeResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WardTypeResponse> getWardTypes() {
        return wardTypeRepository.findAll()
                .stream()
                .map(wardType -> modelMapper.map(wardType, WardTypeResponse.class))
                .toList();
    }

    @Override
    public WardResponse createWard(WardRequest request) {
        WardType wardType = wardTypeRepository.findById(request.getWardTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Ward type not found"));

        String code = request.getCode().trim().toUpperCase();
        String name = request.getName().trim();

        if (wardRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Ward code already exists");
        }

        if (wardRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Ward name already exists");
        }

        Ward ward = new Ward();//modelMapper.map(request, Ward.class);
        ward.setWardType(wardType);
        ward.setCode(code);
        ward.setName(name);
        ward.setDescription(request.getDescription());

        Ward saved = wardRepository.save(ward);

        log.info(
                "Ward created successfully with id={} code={} wardType={}",
                saved.getId(),
                saved.getCode(),
                wardType.getCode()
        );

        return modelMapper.map(saved, WardResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WardResponse> getWards(Long wardTypeId) {
        List<Ward> wards = wardTypeId == null
                ? wardRepository.findByActiveTrue()
                : wardRepository.findByWardTypeIdAndActiveTrue(wardTypeId);

        return wards.stream()
                .map(ward -> modelMapper.map(ward, WardResponse.class))
                .toList();
    }

    @Override
    public RoomResponse createRoom(RoomRequest request) {
        Ward ward = wardRepository.findById(request.getWardId())
                .orElseThrow(() -> new IllegalArgumentException("Ward not found"));

        if (!Boolean.TRUE.equals(ward.getActive())) {
            throw new IllegalArgumentException("Ward is inactive");
        }

        String code = request.getCode().trim().toUpperCase();

        if (roomRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Room code already exists");
        }

        Room room = new Room();//modelMapper.map(request, Room.class);
        room.setWard(ward);
        room.setCode(code);
        room.setName(request.getName().trim());
        room.setDescription(request.getDescription());

        Room saved = roomRepository.save(room);
        log.info(
                "Room created successfully with id={} code={} wardId={}",
                saved.getId(),
                saved.getCode(),
                ward.getId()
        );

        return modelMapper.map(saved, RoomResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRooms(Long wardId) {
        if (!wardRepository.existsById(wardId)) {
            throw new IllegalArgumentException("Ward not found");
        }

        return roomRepository.findByWardIdAndActiveTrue(wardId)
                .stream()
                .map(room -> modelMapper.map(room, RoomResponse.class))
                .toList();
    }

    @Override
    public BedResponse createBed(BedRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!Boolean.TRUE.equals(room.getActive())) {
            throw new IllegalArgumentException("Room is inactive");
        }

        if (!Boolean.TRUE.equals(room.getWard().getActive())) {
            throw new IllegalArgumentException("Ward is inactive");
        }

        String code = request.getCode().trim().toUpperCase();

        if (bedRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Bed code already exists");
        }

        Bed bed = new Bed();//modelMapper.map(request, Bed.class);
        bed.setRoom(room);
        bed.setCode(code);
        bed.setName(request.getName().trim());
        bed.setStatus(request.getStatus());

        if (request.getStatus() == null) {
            bed.setStatus(BedStatus.AVAILABLE);
        }

        Bed saved = bedRepository.save(bed);

        log.info(
                "Bed created successfully with id={} code={} roomId={}",
                saved.getId(),
                saved.getCode(),
                room.getId()
        );

        return modelMapper.map(saved, BedResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BedResponse> getBeds(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Room not found");
        }
        List<Bed> byRoomIdAndActiveTrue = bedRepository.findByRoomIdAndActiveTrue(roomId);

        return byRoomIdAndActiveTrue
                .stream()
                .map(bed -> modelMapper.map(bed, BedResponse.class))
                .toList();
    }

    @Override
    public BedResponse assignBed(BedAssignRequest request) {
        Bed bed = bedRepository.findById(request.getBedId())
                .orElseThrow(() -> new IllegalArgumentException("Bed not found"));

        if (!Boolean.TRUE.equals(bed.getActive())) {
            throw new IllegalArgumentException("Bed is inactive");
        }

        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new IllegalArgumentException(
                    "Bed is not available. Current status: " + bed.getStatus()
            );
        }

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(()-> new ResourceNotFoundException("Patient not found!"));
        bed.setStatus(BedStatus.OCCUPIED);

        Bed saved = bedRepository.save(bed);

        log.info(
                "Bed assigned successfully. bedId={}",
                saved.getId()
        );

        return modelMapper.map(saved, BedResponse.class);
    }
}
