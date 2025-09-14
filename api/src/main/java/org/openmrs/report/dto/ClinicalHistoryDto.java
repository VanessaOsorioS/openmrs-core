package org.openmrs.report.dto;

import java.util.List;

public class ClinicalHistoryDto  {
    private PatientSummaryDto patient;
    private List<VisitDto> visits;

    public PatientSummaryDto getPatient() {
        return patient;
    }
    public void setPatient(PatientSummaryDto patient) {
        this.patient = patient;
    }
    public List<VisitDto> getVisits() {
        return visits;
    }
    public void setVisits(List<VisitDto> visits) {
        this.visits = visits;
    }
}
