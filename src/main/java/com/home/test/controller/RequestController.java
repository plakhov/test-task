package com.home.test.controller;

import com.home.test.dto.RequestRecord;
import com.home.test.dto.UserRecord;
import com.home.test.entity.Role;
import com.home.test.exception.ForbiddenException;
import com.home.test.service.RequestService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/requests")
public class RequestController {
    private static final Logger LOG = LoggerFactory.getLogger(RequestController.class);
    private static final int ROW_PER_PAGE = 50;

    private final RequestService requestService;
    private final Configuration configuration;

    public RequestController(RequestService requestService, Configuration configuration) {
        this.requestService = requestService;
        this.configuration = configuration;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @Secured("ROLE_ADMIN")
    public void getRequests(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                            HttpServletResponse response) {
        List<RequestRecord> requests = requestService.getAll(PageRequest.of(pageNumber, ROW_PER_PAGE));
        long count = requestService.count();
        boolean hasPrev = pageNumber > 0;
        boolean hasNext = requests.size() == ROW_PER_PAGE;
        Map<String, Object> model = new HashMap<>();
        model.put("my", false);
        model.put("rowPerPage", ROW_PER_PAGE);
        model.put("currentPage", pageNumber + 1);
        model.put("allPages", count / ROW_PER_PAGE + 1);
        model.put("requests", requests);
        model.put("hasPrev", hasPrev);
        model.put("prev", pageNumber - 1);
        model.put("hasNext", hasNext);
        model.put("next", pageNumber + 1);
        Template template = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            template = configuration.getTemplate("requests.ftl");
            template.process(model, response.getWriter());
        } catch (IOException | TemplateException e) {
            LOG.error("Got exception while prepare users page", e);
        }
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/my")
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void getRequestsByUser(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                                  @AuthenticationPrincipal UserRecord record,
                                  HttpServletResponse response) {
        List<RequestRecord> requests = requestService.getByUser(record, PageRequest.of(pageNumber, ROW_PER_PAGE));
        long count = requestService.countByUserId(Long.parseLong(record.getId()));
        boolean hasPrev = pageNumber > 0;
        boolean hasNext = requests.size() == ROW_PER_PAGE;
        Map<String, Object> model = new HashMap<>();
        model.put("my", true);
        model.put("rowPerPage", ROW_PER_PAGE);
        model.put("currentPage", pageNumber + 1);
        model.put("allPages", count / ROW_PER_PAGE + 1);
        model.put("requests", requests);
        model.put("hasPrev", hasPrev);
        model.put("prev", pageNumber - 1);
        model.put("hasNext", hasNext);
        model.put("next", pageNumber + 1);
        Template template = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            template = configuration.getTemplate("requests.ftl");
            template.process(model, response.getWriter());
        } catch (IOException | TemplateException e) {
            LOG.error("Got exception while prepare users page", e);
        }
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/add")
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void createRequest(HttpServletResponse response) {
        Map<String, Object> model = new HashMap<>();
        Template template = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            template = configuration.getTemplate("request.ftl");
            template.process(model, response.getWriter());
        } catch (IOException | TemplateException e) {
            LOG.error("Got exception while prepare request add page", e);
        }
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/{requestId}/edit")
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void editRequest(@PathVariable("requestId") long requestId,
                                  @AuthenticationPrincipal UserRecord userRecord,
                                  HttpServletResponse response) {
        Map<String, Object> model = new HashMap<>();
        requestService.getById(requestId).ifPresent(requestRecord -> {
            if (userRecord.getRole() == Role.ROLE_USER && !userRecord.getId().equals(Long.toString(requestRecord.getUserId()))) {
                throw new ForbiddenException();
            }
            model.put("request", requestRecord);
        });
        Template template = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            template = configuration.getTemplate("request.ftl");
            template.process(model, response.getWriter());
        } catch (IOException | TemplateException e) {
            LOG.error("Got exception while prepare request add page", e);
        }
    }

    @PostMapping(value = "/add")
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void createRequest(RequestRecord record,
                              @AuthenticationPrincipal UserRecord userRecord,
                              HttpServletResponse response) {
        record.setUserId(Long.parseLong(userRecord.getId()));
        boolean success = false;
        if (StringUtils.isEmpty(record.getId())) {
            success = requestService.create(record);
        } else {
            success = requestService.update(record);
        }
        if (success) {
            try {
                if (userRecord.getRole() == Role.ROLE_USER) {
                    response.sendRedirect("/requests/my");
                } else {
                    response.sendRedirect("/requests");
                }
            } catch (IOException e) {
                LOG.error("Got exception while execute redirect", e);
            }
            getRequestsByUser(0, userRecord, response);
        }
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/{requestId}/delete")
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void deleteRequest(@PathVariable("requestId") long requestId,
                              @AuthenticationPrincipal UserRecord userRecord,
                              HttpServletResponse response) {
        requestService.getById(requestId).ifPresent(requestRecord -> {
            if (userRecord.getRole() == Role.ROLE_USER && !userRecord.getId().equals(Long.toString(requestRecord.getUserId()))) {
                throw new ForbiddenException();
            }
        });
        if (requestService.remove(requestId)) {
            try {
                if (userRecord.getRole() == Role.ROLE_USER) {
                    response.sendRedirect("/requests/my");
                } else {
                    response.sendRedirect("/requests");
                }
            } catch (IOException e) {
                LOG.error("Got exception while execute redirect", e);
            }
            getRequestsByUser(0, userRecord, response);
        }
    }

}
