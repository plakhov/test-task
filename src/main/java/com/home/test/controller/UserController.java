package com.home.test.controller;


import com.home.test.dto.RequestRecord;
import com.home.test.dto.UserRecord;
import com.home.test.service.UserService;
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
@RequestMapping(value = "/users")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final int ROW_PER_PAGE = 50;

    private final UserService userService;
    private final Configuration configuration;

    public UserController(UserService userService, Configuration configuration) {
        this.userService = userService;
        this.configuration = configuration;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public void getUsers(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                         HttpServletResponse response) {
        List<UserRecord> requests = userService.getAll(PageRequest.of(pageNumber, ROW_PER_PAGE));
        long count = userService.count();
        boolean hasPrev = pageNumber > 0;
        boolean hasNext = requests.size() == ROW_PER_PAGE;
        Map<String, Object> model = new HashMap<>();
        model.put("rowPerPage", ROW_PER_PAGE);
        model.put("currentPage", pageNumber+1);
        model.put("allPages", count/ROW_PER_PAGE+1);
        model.put("users", requests);
        model.put("hasPrev", hasPrev);
        model.put("prev", pageNumber - 1);
        model.put("hasNext", hasNext);
        model.put("next", pageNumber + 1);
        Template template = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            template = configuration.getTemplate("users.ftl");
            template.process(model, response.getWriter());
        } catch (IOException | TemplateException e) {
            LOG.error("Got exception while prepare users page",e);
        }
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/add")
    @Secured(value = "ROLE_ADMIN")
    public void createUser(HttpServletResponse response) {
        Map<String, Object> model = new HashMap<>();
        Template template = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            template = configuration.getTemplate("user.ftl");
            template.process(model, response.getWriter());
        } catch (IOException | TemplateException e) {
            LOG.error("Got exception while prepare request add page", e);
        }
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/{userId}/edit")
    @Secured(value = "ROLE_ADMIN")
    public void editUser(@PathVariable("userId") long userId,
                                  HttpServletResponse response) {
        Map<String, Object> model = new HashMap<>();
        userService.getById(userId).ifPresent(requestRecord -> {
            model.put("user", requestRecord);
        });
        Template template = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            template = configuration.getTemplate("user.ftl");
            template.process(model, response.getWriter());
        } catch (IOException | TemplateException e) {
            LOG.error("Got exception while prepare request add page", e);
        }
    }

    @PostMapping(value = "/add")
    @Secured(value = "ROLE_ADMIN")
    public void createUser(UserRecord record,
                              HttpServletResponse response) {
        boolean success = false;
        if (StringUtils.isEmpty(record.getId())) {
            success = userService.create(record);
        } else {
            success = userService.update(record);
        }
        if (success) {
            try {
                response.sendRedirect("/users");
            } catch (IOException e) {
                LOG.error("Got exception while execute redirect", e);
            }
            getUsers(0, response);
        }
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/{userId}/delete")
    @Secured(value = "ROLE_ADMIN")
    public void deleteUser(@PathVariable("userId") long userId,
                              HttpServletResponse response) {
        if (userService.remove(userId)) {
            try {
                response.sendRedirect("/users");
            } catch (IOException e) {
                LOG.error("Got exception while execute redirect", e);
            }
            getUsers(0, response);
        }
    }

}
