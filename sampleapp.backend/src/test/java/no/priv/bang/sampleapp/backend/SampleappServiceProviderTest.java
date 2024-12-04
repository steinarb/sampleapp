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
package no.priv.bang.sampleapp.backend;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

import static no.priv.bang.sampleapp.services.SampleappConstants.*;
import no.priv.bang.sampleapp.db.liquibase.test.SampleappTestDbLiquibaseRunner;
import no.priv.bang.sampleapp.services.beans.CounterIncrementStepBean;
import no.priv.bang.sampleapp.services.beans.LocaleBean;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;
import no.priv.bang.osgiservice.users.Role;
import no.priv.bang.osgiservice.users.UserManagementService;

class SampleappServiceProviderTest {
    private static final Locale NB_NO = Locale.forLanguageTag("nb-no");

    private static DataSource datasource;

    @BeforeAll
    static void commonSetupForAllTests() throws Exception {
        var derbyDataSourceFactory = new DerbyDataSourceFactory();
        var properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:sampleapp;create=true");
        datasource = derbyDataSourceFactory.createDataSource(properties);
        var runner = new SampleappTestDbLiquibaseRunner();
        runner.activate();
        runner.prepare(datasource);
    }

    @Test
    void testGetAccounts() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var accountsBefore = provider.getAccounts();
        assertThat(accountsBefore).isEmpty();
        assertThat(provider.getCounterIncrementStep("jad")).isEmpty();
        assertThat(provider.getCounter("jad")).isEmpty();
        var accountCreated = provider.lazilyCreateAccount("jad");
        assertTrue(accountCreated);
        var accountsAfter = provider.getAccounts();
        assertThat(accountsAfter).isNotEmpty();
        var defaultInitialCounterIncrementStepValue = 1;
        var counterIncrementStep = provider.getCounterIncrementStep("jad");
        assertThat(counterIncrementStep).isNotEmpty();
        assertEquals(defaultInitialCounterIncrementStepValue, counterIncrementStep.get().counterIncrementStep());
        var defaultInitialCounterValue = 0;
        var counter = provider.getCounter("jad");
        assertThat(counter).isNotEmpty();
        assertEquals(defaultInitialCounterValue, counter.get().counter());
        var secondAccountCreate = provider.lazilyCreateAccount("jad");
        assertFalse(secondAccountCreate);
        var accountsAfterSecondCreate = provider.getAccounts();
        assertThat(accountsAfterSecondCreate).isEqualTo(accountsAfter);
    }

    @Test
    void testThatRoleIsAddedIfMissing() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        verify(useradmin, times(1)).addRole(any());
    }

    @Test
    void testThatRoleIsNotAddedIfPresent() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        when(useradmin.getRoles()).thenReturn(Collections.singletonList(Role.with().rolename(SAMPLEAPPUSER_ROLE).build()));
        var provider = new SampleappServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        verify(useradmin, never()).addRole(any());
    }

    @Test
    void testGetAccountsWithSQLException() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var accounts = provider.getAccounts();
        assertThat(accounts).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testLazilyCreateAccountWithSQLException() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var accountCreated = provider.lazilyCreateAccount("jad");
        assertFalse(accountCreated);
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testIncrementAndDecrement() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        // Create new account with default values for counter and increment step
        provider.lazilyCreateAccount("on");
        var initialCounterIncrementStep = provider.getCounterIncrementStep("on").orElseThrow();
        var initialCounterValue = provider.getCounter("on").orElseThrow();

        // Set the increment step to the existing step value plus one
        var newIncrementStep = CounterIncrementStepBean.with()
            .username("on")
            .counterIncrementStep(initialCounterIncrementStep.counterIncrementStep() + 1)
            .build();
        var updatedIncrementStep = provider.updateCounterIncrementStep(newIncrementStep).orElseThrow();
        assertThat(updatedIncrementStep.counterIncrementStep()).isGreaterThan(initialCounterIncrementStep.counterIncrementStep());

        // Increment and verify the expected result
        var expectedIncrementedValue = initialCounterValue.counter() + updatedIncrementStep.counterIncrementStep();
        var incrementedValue = provider.incrementCounter("on").orElseThrow();
        assertEquals(expectedIncrementedValue, incrementedValue.counter());

        // Decrement and verify the expected result
        var decrementedValue = provider.decrementCounter("on").orElseThrow();
        assertEquals(initialCounterValue, decrementedValue);
    }

    @Test
    void testGetCounterIncrementStepWithSQLException() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var incrementStep = provider.getCounterIncrementStep("jad");
        assertThat(incrementStep).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testUpdateCounterIncrementStepWithSQLException() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var updatedIncrementStep = provider.updateCounterIncrementStep(CounterIncrementStepBean.with().build());
        assertThat(updatedIncrementStep).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testGetCounterWithSQLExceptio() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var counter = provider.getCounter("jad");
        assertThat(counter).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testIncrementCounterWithSQLExceptio() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var incrementedCounter = provider.incrementCounter("jad");
        assertThat(incrementedCounter).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testDecrementCounterWithSQLExceptio() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new SampleappServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var decrementedCounter = provider.decrementCounter("jad");
        assertThat(decrementedCounter).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testDefaultLocale() {
        var sampleapp = new SampleappServiceProvider();
        var useradmin = mock(UserManagementService.class);
        sampleapp.setUseradmin(useradmin);
        sampleapp.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertEquals(NB_NO, sampleapp.defaultLocale());
    }

    @Test
    void testAvailableLocales() {
        var sampleapp = new SampleappServiceProvider();
        var useradmin = mock(UserManagementService.class);
        sampleapp.setUseradmin(useradmin);
        sampleapp.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var locales = sampleapp.availableLocales();
        assertThat(locales).isNotEmpty().contains(LocaleBean.with().locale(sampleapp.defaultLocale()).build());
    }

    @Test
    void testDisplayTextsForDefaultLocale() {
        var sampleapp = new SampleappServiceProvider();
        var useradmin = mock(UserManagementService.class);
        sampleapp.setUseradmin(useradmin);
        sampleapp.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var displayTexts = sampleapp.displayTexts(sampleapp.defaultLocale());
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayText() {
        var sampleapp = new SampleappServiceProvider();
        var useradmin = mock(UserManagementService.class);
        sampleapp.setUseradmin(useradmin);
        sampleapp.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var text1 = sampleapp.displayText("hi", "nb_NO");
        assertEquals("Hei", text1);
        var text2 = sampleapp.displayText("hi", "en_GB");
        assertEquals("Hi", text2);
        var text3 = sampleapp.displayText("hi", "");
        assertEquals("Hei", text3);
        var text4 = sampleapp.displayText("hi", null);
        assertEquals("Hei", text4);
    }

}
