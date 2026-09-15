package com.suma.hmis_service.feature.treatment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public TreatmentResponse createTreatment(TreatmentRequest request) {

        Treatment treatment = modelMapper.map(request, Treatment.class);

        Treatment savedTreatment = treatmentRepository.save(treatment);

        return modelMapper.map(savedTreatment, TreatmentResponse.class);
    }

    @Override
    public TreatmentResponse getTreatmentById(Long id) {

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Treatment not found with id: " + id)
                );

        return modelMapper.map(treatment, TreatmentResponse.class);
    }

    @Override
    public List<TreatmentResponse> getAllTreatments() {

        return treatmentRepository.findAll()
                .stream()
                .map(treatment ->
                        modelMapper.map(treatment, TreatmentResponse.class)
                )
                .toList();
    }

    @Override
    public TreatmentResponse updateTreatment(
            Long id,
            TreatmentRequest request
    ) {

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Treatment not found with id: " + id)
                );

        modelMapper.map(request, treatment);

        Treatment updatedTreatment = treatmentRepository.save(treatment);

        return modelMapper.map(updatedTreatment, TreatmentResponse.class);
    }

    @Override
    public void deleteTreatment(Long id) {

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Treatment not found with id: " + id)
                );

        treatmentRepository.delete(treatment);
    }
}
