package com.home.test.controller;

import com.home.test.dto.UserRecord;
import com.home.test.entity.Role;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class IndexController {
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    private final Configuration configuration;

    public IndexController(Configuration configuration) {
        this.configuration = configuration;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public void getUsers(@AuthenticationPrincipal UserRecord record,
            HttpServletResponse response) {
        Map<String, Object> model = new HashMap<>();
        model.put("admin", record.getRole() == Role.ROLE_ADMIN);
        Template template = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            template = configuration.getTemplate("index.ftl");
            template.process(model, response.getWriter());
        } catch (IOException | TemplateException e) {
            LOG.error("Got exception while prepare users page",e);
        }
    }
}
