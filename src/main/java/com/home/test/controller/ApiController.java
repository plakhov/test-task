package com.home.test.controller;

import com.home.test.dto.RequestRecord;
import com.home.test.dto.UserRecord;
import com.home.test.exception.NotFoundException;
import com.home.test.service.RequestService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RequestService requestService;

    public ApiController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(value = "/requests/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RequestRecord getById(@PathVariable("requestId") long requestId,
                                 @AuthenticationPrincipal UserRecord record) {
        return requestService.getById(requestId, record).orElseThrow(NotFoundException::new);
    }

    @PostMapping(value = "/requests", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RequestRecord create(@RequestBody RequestRecord requestRecord,
                                @AuthenticationPrincipal UserRecord record) {
        if (requestRecord.getUserId() <= 0) {
            requestRecord.setUserId(Long.parseLong(record.getId()));
        }
        return requestService.create(requestRecord).orElse(new RequestRecord());
    }

}
