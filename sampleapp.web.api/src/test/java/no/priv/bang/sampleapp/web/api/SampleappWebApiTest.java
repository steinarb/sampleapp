/*
 * Copyright 2021-2024 Steinar Bang
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ServerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.log.LogService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import no.priv.bang.sampleapp.services.SampleappService;
import no.priv.bang.sampleapp.services.beans.Account;
import no.priv.bang.sampleapp.services.beans.CounterBean;
import no.priv.bang.sampleapp.services.beans.CounterIncrementStepBean;
import no.priv.bang.sampleapp.services.beans.Credentials;
import no.priv.bang.sampleapp.services.beans.LocaleBean;
import no.priv.bang.sampleapp.web.api.resources.ErrorMessage;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;
import no.priv.bang.osgiservice.users.UserManagementService;

class SampleappWebApiTest extends ShiroTestBase {
    private final static Locale NB_NO = Locale.forLanguageTag("nb-no");
    private final static Locale EN_UK = Locale.forLanguageTag("en-uk");

    public static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .findAndRegisterModules();

    @BeforeEach
    void beforeEachTest() {
        removeWebSubjectFromThread();
    }

    @Test
    void testLogin() throws Exception {
        var username = "jd";
        var password = Base64.getEncoder().encodeToString("johnnyBoi".getBytes());
        var credentials = Credentials.with().username(username).password(password).build();
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        createSubjectAndBindItToThread();
        var request = buildPostUrl("/login");
        var postBody = mapper.writeValueAsString(credentials);
        request.setBodyContent(postBody);
        var response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        var username = "jd";
        var password = Base64.getEncoder().encodeToString("johnniBoi".getBytes());
        var credentials = Credentials.with().username(username).password(password).build();
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        createSubjectAndBindItToThread();
        var request = buildPostUrl("/login");
        var postBody = mapper.writeValueAsString(credentials);
        request.setBodyContent(postBody);
        var response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetAccounts() throws Exception {
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var account = Account.with().accountId(123).build();
        when(sampleapp.getAccounts()).thenReturn(Collections.singletonList(account));
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/accounts");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jd", "johnnyBoi");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        var accounts = mapper.readValue(response.getOutputStreamBinaryContent(), new TypeReference<List<Account>>() {});
        assertThat(accounts).isNotEmpty();
    }

    @Test
    void testGetCounterIncrementStep() throws Exception {
        var incrementStepValue = 1;
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var optionalIncrementStep = Optional.of(CounterIncrementStepBean.with().counterIncrementStep(incrementStepValue).build());
        when(sampleapp.getCounterIncrementStep(anyString())).thenReturn(optionalIncrementStep);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/incrementstep/jad");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        CounterIncrementStepBean bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterIncrementStepBean.class);
        assertEquals(incrementStepValue, bean.counterIncrementStep());
    }

    @Test
    void testGetCounterIncrementStepWhenNotLoggedIn() throws Exception {
        var incrementStepValue = 1;
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var optionalIncrementStep = Optional.of(CounterIncrementStepBean.with().counterIncrementStep(incrementStepValue).build());
        when(sampleapp.getCounterIncrementStep(anyString())).thenReturn(optionalIncrementStep);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/incrementstep/jad");
        var response = new MockHttpServletResponse();

        createSubjectAndBindItToThread();
        servlet.service(request, response);
        assertEquals(401, response.getStatus());
    }

    @Test
    void testGetCounterIncrementStepWhenUserLacksRole() throws Exception {
        var incrementStepValue = 1;
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var optionalIncrementStep = Optional.of(CounterIncrementStepBean.with().counterIncrementStep(incrementStepValue).build());
        when(sampleapp.getCounterIncrementStep(anyString())).thenReturn(optionalIncrementStep);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/incrementstep/jad");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jd", "johnnyBoi");
        servlet.service(request, response);
        assertEquals(403, response.getStatus());
    }

    @Test
    void testGetCounterIncrementStepWhenNotFound() throws Exception {
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/incrementstep/jad");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testUpdateCounterIncrementStep() throws Exception {
        var incrementStepValue = 1;
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var optionalIncrementStep = Optional.of(CounterIncrementStepBean.with().counterIncrementStep(incrementStepValue).build());
        when(sampleapp.updateCounterIncrementStep(any())).thenReturn(optionalIncrementStep);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var updateIncrementStep = CounterIncrementStepBean.with()
            .username("jad")
            .counterIncrementStep(incrementStepValue)
            .build();
        var request = buildPostUrl("/counter/incrementstep");
        var postBody = mapper.writeValueAsString(updateIncrementStep);
        request.setBodyContent(postBody);
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        var bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterIncrementStepBean.class);
        assertEquals(incrementStepValue, bean.counterIncrementStep());
    }

    @Test
    void testUpdateCounterIncrementStepWhenUpdateFails() throws Exception {
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var updateIncrementStep = CounterIncrementStepBean.with()
            .username("jad")
            .counterIncrementStep(3)
            .build();
        var request = buildPostUrl("/counter/incrementstep");
        var postBody = mapper.writeValueAsString(updateIncrementStep);
        request.setBodyContent(postBody);
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(500, response.getStatus());
    }

    @Test
    void testGetCounter() throws Exception {
        var counterValue = 3;
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var optionalCounter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.getCounter(anyString())).thenReturn(optionalCounter);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/jad");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        var bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterBean.class);
        assertEquals(counterValue, bean.counter());
    }

    @Test
    void testGetCounterWhenNotFound() throws Exception {
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/jad");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testIncrementCounter() throws Exception {
        var counterValue = 3;
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var optionalCounter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.incrementCounter(anyString())).thenReturn(optionalCounter);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/jad/increment");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        var bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterBean.class);
        assertEquals(counterValue, bean.counter());
    }

    @Test
    void testIncrementCounterWhenFailing() throws Exception {
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/jad/increment");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(500, response.getStatus());
    }

    @Test
    void testDecrementCounter() throws Exception {
        var counterValue = 3;
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var optionalCounter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.decrementCounter(anyString())).thenReturn(optionalCounter);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/jad/decrement");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        var bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterBean.class);
        assertEquals(counterValue, bean.counter());
    }

    @Test
    void testDecrementCounterWhenFailing() throws Exception {
        var logservice = new MockLogService();
        var sampleapp = mock(SampleappService.class);
        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);
        var request = buildGetUrl("/counter/jad/decrement");
        var response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(500, response.getStatus());
    }

    @Test
    void testDefaultLocale() throws Exception {
        // Set up REST API servlet with mocked services
        var sampleapp = mock(SampleappService.class);
        when(sampleapp.defaultLocale()).thenReturn(NB_NO);
        var logservice = new MockLogService();

        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);

        // Create the request and response
        var request = buildGetUrl("/defaultlocale");
        var response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        var defaultLocale = mapper.readValue(response.getOutputStreamBinaryContent(), Locale.class);
        assertEquals(NB_NO, defaultLocale);
    }
    @Test
    void testAvailableLocales() throws Exception {
        // Set up REST API servlet with mocked services
        var sampleapp = mock(SampleappService.class);
        when(sampleapp.availableLocales()).thenReturn(Collections.singletonList(Locale.forLanguageTag("nb-NO")).stream().map(l -> LocaleBean.with().locale(l).build()).collect(Collectors.toList()));
        MockLogService logservice = new MockLogService();

        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);

        // Create the request and response
        var request = buildGetUrl("/availablelocales");
        var response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        var availableLocales = mapper.readValue(response.getOutputStreamBinaryContent(), new TypeReference<List<LocaleBean>>() {});
        assertThat(availableLocales).isNotEmpty().contains(LocaleBean.with().locale(Locale.forLanguageTag("nb-NO")).build());
    }

    @Test
    void testDisplayTexts() throws Exception {
        // Set up REST API servlet with mocked services
        var sampleapp = mock(SampleappService.class);
        var texts = new HashMap<String, String>();
        texts.put("date", "Dato");
        when(sampleapp.displayTexts(NB_NO)).thenReturn(texts);
        var logservice = new MockLogService();

        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);

        // Create the request and response
        var request = buildGetUrl("/displaytexts");
        request.setQueryString("locale=nb_NO");
        var response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        var displayTexts = mapper.readValue(response.getOutputStreamBinaryContent(), new TypeReference<Map<String, String>>() {});
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayTextsWithUnknownLocale() throws Exception {
        // Set up REST API servlet with mocked services
        var sampleapp = mock(SampleappService.class);
        var texts = new HashMap<>();
        texts.put("date", "Dato");
        when(sampleapp.displayTexts(EN_UK)).thenThrow(MissingResourceException.class);
        var logservice = new MockLogService();

        var useradmin = mock(UserManagementService.class);
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , useradmin, logservice);

        // Create the request and response
        var request = buildGetUrl("/displaytexts");
        request.setQueryString("locale=en_UK");
        var response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(500, response.getStatus());
        assertEquals("application/json", response.getContentType());
        var errorMessage = mapper.readValue(response.getOutputStreamBinaryContent(), ErrorMessage.class);
        assertEquals(500, errorMessage.getStatus());
        assertThat(errorMessage.getMessage()).startsWith("Unknown locale");
    }

    private MockHttpServletRequest buildGetUrl(String resource) {
        var request = buildRequest(resource);
        request.setMethod("GET");
        return request;
    }

    private MockHttpServletRequest buildPostUrl(String resource) {
        var contenttype = MediaType.APPLICATION_JSON;
        var request = buildRequest(resource);
        request.setMethod("POST");
        request.setContentType(contenttype);
        request.addHeader("Content-Type", contenttype);
        return request;
    }

    private MockHttpServletRequest buildRequest(String resource) {
        var session = new MockHttpSession();
        var request = new MockHttpServletRequest();
        request.setProtocol("HTTP/1.1");
        request.setRequestURL("http://localhost:8181/sampleapp/api" + resource);
        request.setRequestURI("/sampleapp/api" + resource);
        request.setContextPath("/sampleapp");
        request.setServletPath("/api");
        request.setSession(session);
        return request;
    }

    private SampleappWebApi simulateDSComponentActivationAndWebWhiteboardConfiguration(SampleappService sampleapp, UserManagementService useradmin, LogService logservice) throws Exception {
        var servlet = new SampleappWebApi();
        servlet.setLogService(logservice);
        servlet.setSampleappService(sampleapp);
        servlet.setUseradmin(useradmin);
        servlet.activate();
        var config = createServletConfigWithApplicationAndPackagenameForJerseyResources();
        servlet.init(config);
        return servlet;
    }

    private ServletConfig createServletConfigWithApplicationAndPackagenameForJerseyResources() {
        var config = mock(ServletConfig.class);
        when(config.getInitParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(ServerProperties.PROVIDER_PACKAGES)));
        when(config.getInitParameter(ServerProperties.PROVIDER_PACKAGES)).thenReturn("no.priv.bang.sampleapp.web.api.resources");
        var servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("/sampleapp");
        when(config.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttributeNames()).thenReturn(Collections.emptyEnumeration());
        return config;
    }
}
