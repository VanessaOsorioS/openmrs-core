package org.openmrs.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.report.ClinicalHistoryReportFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Controller
public class ClinicalHistoryReportsController {

    @Autowired
    private ClinicalHistoryReportFacade facade;

    @RequestMapping(value = "/report-history/{patientUuid}.htm", method = RequestMethod.GET)
    public void getReport(@PathVariable("patientUuid") String patientUuid,
                          HttpServletResponse response,
                          Locale locale) throws Exception {

        if (StringUtils.isBlank(patientUuid)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "patientUuid is required");
            return;
        }

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setContentType("application/pdf");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition",
                "attachment; filename=\"clinical-history-" + patientUuid + ".pdf\"");

        try (OutputStream os = response.getOutputStream()) {
            facade.writePdf(patientUuid, (locale != null ? locale : Context.getLocale()), os);
        }
    }
}
