package org.openmrs.report.dto;

public class ObsDto {
	
	private String uuid;
	
	private String datetime;
	
	private String concept;
	
	private String value;
	
	private String units;
	
	private String codedUuid;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getDatetime() {
		return datetime;
	}
	
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public String getConcept() {
		return concept;
	}
	
	public void setConcept(String concept) {
		this.concept = concept;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getUnits() {
		return units;
	}
	
	public void setUnits(String units) {
		this.units = units;
	}
	
	public String getCodedUuid() {
		return codedUuid;
	}
	
	public void setCodedUuid(String codedUuid) {
		this.codedUuid = codedUuid;
	}
}
