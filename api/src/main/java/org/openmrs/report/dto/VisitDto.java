package org.openmrs.report.dto;

import java.util.List;

public class VisitDto {
    private String uuid;
    private String visitType;
    private String startDatetime;
    private String stopDatetime;
    private String location;
    private List<EncounterDto> encounters;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getVisitType() {
        return visitType;
    }
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }
    public String getStartDatetime() {
        return startDatetime;
    }
    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }
    public String getStopDatetime() {
        return stopDatetime;
    }
    public void setStopDatetime(String stopDatetime) {
        this.stopDatetime = stopDatetime;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public List<EncounterDto> getEncounters() {
        return encounters;
    }
    public void setEncounters(List<EncounterDto> encounters) {
        this.encounters = encounters;
    }
}
