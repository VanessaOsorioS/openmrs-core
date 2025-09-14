package org.openmrs.report.dto;

import java.util.List;

public class EncounterDto {
    private String uuid;
    private String encounterType;
    private String encounterDatetime;
    private String provider;
    private String location;
    private List<ObsDto> observations;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getEncounterType() {
        return encounterType;
    }
    public void setEncounterType(String encounterType) {
        this.encounterType = encounterType;
    }
    public String getEncounterDatetime() {
        return encounterDatetime;
    }
    public void setEncounterDatetime(String encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public List<ObsDto> getObservations() {
        return observations;
    }
    public void setObservations(List<ObsDto> observations) {
        this.observations = observations;
    }
}
