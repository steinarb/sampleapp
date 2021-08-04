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
package no.priv.bang.sampleapp.backend;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

import no.priv.bang.sampleapp.db.liquibase.test.SampleappTestDbLiquibaseRunner;
import no.priv.bang.sampleapp.services.Account;
import no.priv.bang.sampleapp.services.LocaleBean;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;
import no.priv.bang.osgiservice.users.UserManagementService;

class SampleappServiceProviderTest {
    private final static Locale NB_NO = Locale.forLanguageTag("nb-no");

    private static DataSource datasource;

    @BeforeAll
    static void commonSetupForAllTests() throws Exception {
        DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:sampleapp;create=true");
        datasource = derbyDataSourceFactory.createDataSource(properties);
        MockLogService logservice = new MockLogService();
        SampleappTestDbLiquibaseRunner runner = new SampleappTestDbLiquibaseRunner();
        runner.setLogService(logservice);
        runner.activate();
        runner.prepare(datasource);
    }

    @Test
    void testGetAccounts() {
        MockLogService logservice = new MockLogService();
        UserManagementService useradmin = mock(UserManagementService.class);
        SampleappServiceProvider provider = new SampleappServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        List<Account> accounts = provider.getAccounts();
        assertThat(accounts).isNotEmpty();
    }

    @Test
    void testDefaultLocale() {
        SampleappServiceProvider ukelonn = new SampleappServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        ukelonn.setUseradmin(useradmin);
        ukelonn.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertEquals(NB_NO, ukelonn.defaultLocale());
    }

    @Test
    void testAvailableLocales() {
        SampleappServiceProvider ukelonn = new SampleappServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        ukelonn.setUseradmin(useradmin);
        ukelonn.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        List<LocaleBean> locales = ukelonn.availableLocales();
        assertThat(locales).isNotEmpty().contains(LocaleBean.with().locale(ukelonn.defaultLocale()).build());
    }

    @Test
    void testDisplayTextsForDefaultLocale() {
        SampleappServiceProvider ukelonn = new SampleappServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        ukelonn.setUseradmin(useradmin);
        ukelonn.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Map<String, String> displayTexts = ukelonn.displayTexts(ukelonn.defaultLocale());
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayText() {
        SampleappServiceProvider ukelonn = new SampleappServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        ukelonn.setUseradmin(useradmin);
        ukelonn.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        String text1 = ukelonn.displayText("hi", "nb_NO");
        assertEquals("Hei", text1);
        String text2 = ukelonn.displayText("hi", "en_GB");
        assertEquals("Hi", text2);
        String text3 = ukelonn.displayText("hi", "");
        assertEquals("Hei", text3);
        String text4 = ukelonn.displayText("hi", null);
        assertEquals("Hei", text4);
    }

}
