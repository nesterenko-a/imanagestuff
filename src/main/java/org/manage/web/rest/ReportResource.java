package org.manage.web.rest;

import org.manage.service.MailService;
import org.manage.service.ReportService;
import org.manage.service.dto.DayReportDTO;
import org.manage.service.dto.ReportSingleDayRequestModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

@Path("/api/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ReportResource {

    private final ReportService reportService;
    private final MailService mailService;

    @Inject
    public ReportResource(ReportService reportService, MailService mailService) {
        this.reportService = reportService;
        this.mailService = mailService;
    }

    @POST
    @Path("day-report")
    public CompletionStage<Response> sendDayReport(@Valid ReportSingleDayRequestModel reportRequest) {
        final DayReportDTO dayReportDTO = reportService.generateReport(reportRequest.projectId, reportRequest.dateFrom, reportRequest.dateFrom);
        return mailService.sendDayReport(dayReportDTO)
            .thenApply(x -> Response.accepted().build());
    }

    @GET
    @Path("project/{id}")
    public Response projectReport(@PathParam("id") Long projectId, @QueryParam("dateFrom") LocalDate dateFrom, @QueryParam("dateTo") LocalDate dateTo) {
        return Response.ok(reportService.generateReport(projectId, dateFrom, dateTo))
            .build();
    }


}
