
package org.openmrs.report.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.report.ClinicalHistoryReportFacade;
import org.openmrs.report.dto.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("clinicalHistoryReportFacade")
public class ClinicalHistoryReportFacadeImpl implements ClinicalHistoryReportFacade {

    private static final String DATE_FMT = "yyyy-MM-dd";
    private static final String DATETIME_FMT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String templateClasspathPath = "report-templates/clinical-history.ftl";

    @Override
    @Transactional(readOnly = true)
    public void writePdf(String patientUuid, Locale locale, OutputStream out) {
        Patient patient = Context.getPatientService().getPatientByUuid(patientUuid);
        ClinicalHistoryDto dto = build(patient, locale);
        String html = renderHtml(dto, locale, templateClasspathPath);
        renderPdf(html, out);
    }

    private ClinicalHistoryDto build(Patient patient, Locale locale) {
        ClinicalHistoryDto dto = new ClinicalHistoryDto();
        dto.setPatient(toPatientSummary(patient));
        List<Visit> visits = Context.getVisitService().getVisitsByPatient(patient);
        visits.sort(Comparator.comparing(Visit::getStartDatetime, Comparator.nullsLast(Comparator.naturalOrder())));
        List<VisitDto> visitDtos = new ArrayList<>();
        for (Visit v : visits) {
            VisitDto vdto = new VisitDto();
            vdto.setUuid(v.getUuid());
            vdto.setVisitType(v.getVisitType() != null ? v.getVisitType().getName() : null);
            vdto.setStartDatetime(fmtDateTime(v.getStartDatetime()));
            vdto.setStopDatetime(fmtDateTime(v.getStopDatetime()));
            vdto.setLocation(v.getLocation() != null ? v.getLocation().getName() : null);
            List<Encounter> encs = new ArrayList<>(v.getEncounters());
            encs.sort(Comparator.comparing(Encounter::getEncounterDatetime, Comparator.nullsLast(Comparator.naturalOrder())));
            List<EncounterDto> encDtos = new ArrayList<>();
            for (Encounter e : encs) {
                EncounterDto edto = new EncounterDto();
                edto.setUuid(e.getUuid());
                edto.setEncounterType(e.getEncounterType() != null ? e.getEncounterType().getName() : null);
                edto.setEncounterDatetime(fmtDateTime(e.getEncounterDatetime()));
                edto.setLocation(e.getLocation() != null ? e.getLocation().getName() : null);
                edto.setProvider(firstProviderName(e));
                List<Obs> obs = new ArrayList<>(e.getAllObs(true));
                obs.sort(Comparator.comparing(Obs::getObsDatetime, Comparator.nullsLast(Comparator.naturalOrder())));
                List<ObsDto> obsDtos = new ArrayList<>();
                for (Obs o : obs) {
                    ObsDto odto = new ObsDto();
                    odto.setUuid(o.getUuid());
                    odto.setDatetime(fmtDateTime(o.getObsDatetime()));
                    odto.setConcept(bestConceptName(o.getConcept(), locale));
                    odto.setValue(o.getValueAsString(locale));
                    if (o.getConcept() instanceof org.openmrs.ConceptNumeric) {
                        String units = ((org.openmrs.ConceptNumeric) o.getConcept()).getUnits();
                        if (units != null) odto.setUnits(units);
                    }
                    if (o.getValueCoded() != null) odto.setCodedUuid(o.getValueCoded().getUuid());
                    obsDtos.add(odto);
                }
                edto.setObservations(obsDtos);
                encDtos.add(edto);
            }
            vdto.setEncounters(encDtos);
            visitDtos.add(vdto);
        }
        dto.setVisits(visitDtos);
        return dto;
    }

    private String renderHtml(ClinicalHistoryDto dto, Locale locale, String templateClasspathPath) {
        try {
            Configuration cfg = new Configuration(new Version("2.3.32"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            Template tpl = cfg.getTemplate(templateClasspathPath, "UTF-8");
            Map<String, Object> model = new HashMap<>();
            model.put("dto", dto);
            model.put("locale", locale);
            model.put("generatedAt", new SimpleDateFormat(DATETIME_FMT).format(new Date()));
            try (StringWriter sw = new StringWriter()) {
                tpl.process(model, sw);
                return sw.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to render HTML template: " + templateClasspathPath, e);
        }
    }

    private void renderPdf(String html, OutputStream out) {
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private PatientSummaryDto toPatientSummary(Patient p) {
        PatientSummaryDto ps = new PatientSummaryDto();
        ps.setUuid(p.getUuid());
        ps.setIdentifier(p.getPatientIdentifier() != null ? p.getPatientIdentifier().getIdentifier() : null);
        if (p.getPersonName() != null) {
            ps.setGivenName(p.getPersonName().getGivenName());
            ps.setFamilyName(p.getPersonName().getFamilyName());
        }
        ps.setGender(p.getGender());
        ps.setBirthdate(p.getBirthdate() != null ? new SimpleDateFormat(DATE_FMT).format(p.getBirthdate()) : null);
        return ps;
    }

    private String firstProviderName(Encounter e) {
        for (EncounterProvider ep : e.getEncounterProviders()) {
            Provider p = ep.getProvider();
            if (p != null) {
                if (p.getName() != null) return p.getName();
                if (p.getIdentifier() != null) return p.getIdentifier();
            }
        }
        return null;
    }

    private String bestConceptName(Concept c, Locale locale) {
        if (c == null) return null;
        if (locale != null) {
            if (c.getName(locale, false) != null) return c.getName(locale, false).getName();
            if (c.getPreferredName(locale) != null) return c.getPreferredName(locale).getName();
            if (c.getFullySpecifiedName(locale) != null) return c.getFullySpecifiedName(locale).getName();
        }
    
    if (c.getPreferredName(locale) != null) return c.getPreferredName(locale).getName();
    if (c.getFullySpecifiedName(locale) != null) return c.getFullySpecifiedName(locale).getName();
        return c.getName() != null ? c.getName().getName() : c.getUuid();
    }

    private String fmtDateTime(Date d) {
        return d != null ? new SimpleDateFormat(DATETIME_FMT).format(d) : null;
    }
}
