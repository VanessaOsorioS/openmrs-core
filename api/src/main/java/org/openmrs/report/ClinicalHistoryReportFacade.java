package org.openmrs.report;

import java.io.OutputStream;
import java.util.Locale;

public interface ClinicalHistoryReportFacade {
	
	void writePdf(String patientUuid, Locale locale, OutputStream out);
}
