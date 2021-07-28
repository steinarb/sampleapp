/*
 * Copyright 2021 Steinar Bang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package no.priv.bang.sampleapp.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ServerProperties;
import org.junit.jupiter.api.Test;
import org.osgi.service.log.LogService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletOutputStream;

import no.priv.bang.sampleapp.services.Account;
import no.priv.bang.sampleapp.services.Credentials;
import no.priv.bang.sampleapp.services.SampleappService;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;

class SampleappWebApiTest extends ShiroTestBase {
    public static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .findAndRegisterModules();

    @Test
    void testLogin() throws Exception {
        String username = "jd";
        String password = "johnnyBoi";
        Credentials credentials = Credentials.with().username(username).password(password).build();
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        createSubjectAndBindItToThread();
        MockHttpServletRequest request = buildPostUrl("/login");
        String postBody = mapper.writeValueAsString(credentials);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());

    }

    @Test
    void testLoginWrongPassword() throws Exception {
        String username = "jd";
        String password = "johnniBoi";
        Credentials credentials = Credentials.with().username(username).password(password).build();
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        createSubjectAndBindItToThread();
        MockHttpServletRequest request = buildPostUrl("/login");
        String postBody = mapper.writeValueAsString(credentials);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetAccounts() throws Exception {
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        Account account = Account.with().accountId(123).build();
        when(sampleapp.getAccounts()).thenReturn(Collections.singletonList(account));
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/accounts");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jd", "johnnyBoi");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        List<Account> accounts = mapper.readValue(getBinaryContent(response), new TypeReference<List<Account>>() {});
        assertThat(accounts).isNotEmpty();
    }

    private byte[] getBinaryContent(MockHttpServletResponse response) throws IOException {
        MockServletOutputStream outputstream = (MockServletOutputStream) response.getOutputStream();
        return outputstream.getBinaryContent();
    }

    private MockHttpServletRequest buildGetUrl(String resource) {
        MockHttpServletRequest request = buildRequest(resource);
        request.setMethod("GET");
        return request;
    }

    private MockHttpServletRequest buildPostUrl(String resource) {
        String contenttype = MediaType.APPLICATION_JSON;
        MockHttpServletRequest request = buildRequest(resource);
        request.setMethod("POST");
        request.setContentType(contenttype);
        request.addHeader("Content-Type", contenttype);
        return request;
    }

    private MockHttpServletRequest buildRequest(String resource) {
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setProtocol("HTTP/1.1");
        request.setRequestURL("http://localhost:8181/sampleapp/api" + resource);
        request.setRequestURI("/sampleapp/api" + resource);
        request.setContextPath("/sampleapp");
        request.setServletPath("/api");
        request.setSession(session);
        return request;
    }

    private SampleappWebApi simulateDSComponentActivationAndWebWhiteboardConfiguration(SampleappService sampleapp, LogService logservice) throws Exception {
        SampleappWebApi servlet = new SampleappWebApi();
        servlet.setLogService(logservice);
        servlet.setSampleappService(sampleapp);
        servlet.activate();
        ServletConfig config = createServletConfigWithApplicationAndPackagenameForJerseyResources();
        servlet.init(config);
        return servlet;
    }

    private ServletConfig createServletConfigWithApplicationAndPackagenameForJerseyResources() {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(ServerProperties.PROVIDER_PACKAGES)));
        when(config.getInitParameter(eq(ServerProperties.PROVIDER_PACKAGES))).thenReturn("no.priv.bang.sampleapp.web.api.resources");
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("/sampleapp");
        when(config.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttributeNames()).thenReturn(Collections.emptyEnumeration());
        return config;
    }
}
