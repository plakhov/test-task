package com.home.test.controller;

import com.home.test.base.AbstractIntegrationTest;
import com.home.test.base.JsonUtils;
import com.home.test.dto.RequestRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.home.test.base.CustomClassResultMatcher.modelMatches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiControllerTest extends AbstractIntegrationTest {


    Authentication authentication;
    RequestRecord createdRequest;

    @BeforeEach
    void setUp() {
        authentication = new TestingAuthenticationToken("admin", "admin",
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_ADMIN"})
    void when_we_create_request_and_then_get_request() throws Exception {
        RequestRecord requestRecord = new RequestRecord();
        requestRecord.setName(FAKER.beer().name());
        requestRecord.setDescription(FAKER.gameOfThrones().quote());
        requestRecord.setUserId(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/requests")
                .principal(authentication)
                .content(JsonUtils.asJsonString(requestRecord))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(modelMatches(RequestRecord.class, model -> {
                    assertThat(model).isNotNull();
                    assertThat(model.getId()).isNotEmpty();
                    assertThat(model).isEqualToIgnoringGivenFields(requestRecord, "id", "userFirstName", "userLastName");
                    createdRequest = model;
                }));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/requests/" + createdRequest.getId())
                .principal(authentication)
                .content(JsonUtils.asJsonString(requestRecord))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(modelMatches(RequestRecord.class, model -> {
                    assertThat(model).isNotNull();
                    assertThat(model.getId()).isNotEmpty();
                    assertThat(model).isEqualTo(createdRequest);
                }));
    }
}