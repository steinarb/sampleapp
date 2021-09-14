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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import org.junit.jupiter.api.Test;
import org.osgi.service.log.LogService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import no.priv.bang.sampleapp.services.Account;
import no.priv.bang.sampleapp.services.CounterBean;
import no.priv.bang.sampleapp.services.CounterIncrementStepBean;
import no.priv.bang.sampleapp.services.Credentials;
import no.priv.bang.sampleapp.services.LocaleBean;
import no.priv.bang.sampleapp.services.SampleappService;
import no.priv.bang.sampleapp.web.api.resources.ErrorMessage;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;

class SampleappWebApiTest extends ShiroTestBase {
    private final static Locale NB_NO = Locale.forLanguageTag("nb-no");
    private final static Locale EN_UK = Locale.forLanguageTag("en-uk");

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
        List<Account> accounts = mapper.readValue(response.getOutputStreamBinaryContent(), new TypeReference<List<Account>>() {});
        assertThat(accounts).isNotEmpty();
    }

    @Test
    void testGetCounterIncrementStep() throws Exception {
        int incrementStepValue = 1;
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        Optional<CounterIncrementStepBean> optionalIncrementStep = Optional.of(CounterIncrementStepBean.with().counterIncrementStep(incrementStepValue).build());
        when(sampleapp.getCounterIncrementStep(anyString())).thenReturn(optionalIncrementStep);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/counter/incrementstep/jad");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        CounterIncrementStepBean bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterIncrementStepBean.class);
        assertEquals(incrementStepValue, bean.getCounterIncrementStep());
    }

    @Test
    void testGetCounterIncrementStepWhenNotFound() throws Exception {
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/counter/incrementstep/jad");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testUpdateCounterIncrementStep() throws Exception {
        int incrementStepValue = 1;
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        Optional<CounterIncrementStepBean> optionalIncrementStep = Optional.of(CounterIncrementStepBean.with().counterIncrementStep(incrementStepValue).build());
        when(sampleapp.updateCounterIncrementStep(any())).thenReturn(optionalIncrementStep);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        CounterIncrementStepBean updateIncrementStep = CounterIncrementStepBean.with()
            .username("jad")
            .counterIncrementStep(incrementStepValue)
            .build();
        MockHttpServletRequest request = buildPostUrl("/counter/incrementstep");
        String postBody = mapper.writeValueAsString(updateIncrementStep);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        CounterIncrementStepBean bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterIncrementStepBean.class);
        assertEquals(incrementStepValue, bean.getCounterIncrementStep());
    }

    @Test
    void testUpdateCounterIncrementStepWhenUpdateFails() throws Exception {
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        CounterIncrementStepBean updateIncrementStep = CounterIncrementStepBean.with()
            .username("jad")
            .counterIncrementStep(3)
            .build();
        MockHttpServletRequest request = buildPostUrl("/counter/incrementstep");
        String postBody = mapper.writeValueAsString(updateIncrementStep);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(500, response.getStatus());
    }

    @Test
    void testGetCounter() throws Exception {
        int counterValue = 3;
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        Optional<CounterBean> optionalCounter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.getCounter(anyString())).thenReturn(optionalCounter);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/counter/jad");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        CounterBean bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterBean.class);
        assertEquals(counterValue, bean.getCounter());
    }

    @Test
    void testGetCounterWhenNotFound() throws Exception {
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/counter/jad");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testIncrementCounter() throws Exception {
        int counterValue = 3;
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        Optional<CounterBean> optionalCounter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.incrementCounter(anyString())).thenReturn(optionalCounter);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/counter/jad/increment");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        CounterBean bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterBean.class);
        assertEquals(counterValue, bean.getCounter());
    }

    @Test
    void testIncrementCounterWhenFailing() throws Exception {
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/counter/jad/increment");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(500, response.getStatus());
    }

    @Test
    void testDecrementCounter() throws Exception {
        int counterValue = 3;
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        Optional<CounterBean> optionalCounter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.decrementCounter(anyString())).thenReturn(optionalCounter);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/counter/jad/decrement");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        CounterBean bean = mapper.readValue(response.getOutputStreamBinaryContent(), CounterBean.class);
        assertEquals(counterValue, bean.getCounter());
    }

    @Test
    void testDecrementCounterWhenFailing() throws Exception {
        MockLogService logservice = new MockLogService();
        SampleappService sampleapp = mock(SampleappService.class);
        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);
        MockHttpServletRequest request = buildGetUrl("/counter/jad/decrement");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jad", "1ad");
        servlet.service(request, response);
        assertEquals(500, response.getStatus());
    }

    @Test
    void testDefaultLocale() throws Exception {
        // Set up REST API servlet with mocked services
        SampleappService sampleapp = mock(SampleappService.class);
        when(sampleapp.defaultLocale()).thenReturn(NB_NO);
        MockLogService logservice = new MockLogService();

        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/defaultlocale");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        Locale defaultLocale = mapper.readValue(response.getOutputStreamBinaryContent(), Locale.class);
        assertEquals(NB_NO, defaultLocale);
    }
    @Test
    void testAvailableLocales() throws Exception {
        // Set up REST API servlet with mocked services
        SampleappService sampleapp = mock(SampleappService.class);
        when(sampleapp.availableLocales()).thenReturn(Collections.singletonList(Locale.forLanguageTag("nb-NO")).stream().map(l -> LocaleBean.with().locale(l).build()).collect(Collectors.toList()));
        MockLogService logservice = new MockLogService();

        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/availablelocales");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<LocaleBean> availableLocales = mapper.readValue(response.getOutputStreamBinaryContent(), new TypeReference<List<LocaleBean>>() {});
        assertThat(availableLocales).isNotEmpty().contains(LocaleBean.with().locale(Locale.forLanguageTag("nb-NO")).build());
    }

    @Test
    void testDisplayTexts() throws Exception {
        // Set up REST API servlet with mocked services
        SampleappService sampleapp = mock(SampleappService.class);
        Map<String, String> texts = new HashMap<>();
        texts.put("date", "Dato");
        when(sampleapp.displayTexts(NB_NO)).thenReturn(texts);
        MockLogService logservice = new MockLogService();

        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/displaytexts");
        request.setQueryString("locale=nb_NO");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        Map<String, String> displayTexts = mapper.readValue(response.getOutputStreamBinaryContent(), new TypeReference<Map<String, String>>() {});
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayTextsWithUnknownLocale() throws Exception {
        // Set up REST API servlet with mocked services
        SampleappService sampleapp = mock(SampleappService.class);
        Map<String, String> texts = new HashMap<>();
        texts.put("date", "Dato");
        when(sampleapp.displayTexts(EN_UK)).thenThrow(MissingResourceException.class);
        MockLogService logservice = new MockLogService();

        SampleappWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(sampleapp , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/displaytexts");
        request.setQueryString("locale=en_UK");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(500, response.getStatus());
        assertEquals("application/json", response.getContentType());
        ErrorMessage errorMessage = mapper.readValue(response.getOutputStreamBinaryContent(), ErrorMessage.class);
        assertEquals(500, errorMessage.getStatus());
        assertThat(errorMessage.getMessage()).startsWith("Unknown locale");
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
