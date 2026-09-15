package com.suma.hmis_service.feature.treatment;

import java.util.List;

public interface TreatmentService {

    TreatmentResponse createTreatment(TreatmentRequest request);

    TreatmentResponse getTreatmentById(Long id);

    List<TreatmentResponse> getAllTreatments();

    TreatmentResponse updateTreatment(Long id, TreatmentRequest request);

    void deleteTreatment(Long id);
}
