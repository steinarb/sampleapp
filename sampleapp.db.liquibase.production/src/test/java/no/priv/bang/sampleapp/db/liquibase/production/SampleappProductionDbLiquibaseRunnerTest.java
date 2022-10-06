/*
 * Copyright 2021-2022 Steinar Bang
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
package no.priv.bang.sampleapp.db.liquibase.production;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

class SampleappProductionDbLiquibaseRunnerTest {

    @Test
    void testCreateAndVerifySomeDataInSomeTables() throws Exception {
        DataSourceFactory dataSourceFactory = new DerbyDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:sampleapp;create=true");
        DataSource datasource = dataSourceFactory.createDataSource(properties);

        SampleappProductionDbLiquibaseRunner runner = new SampleappProductionDbLiquibaseRunner();
        runner.activate();
        runner.prepare(datasource);
        addAccount(datasource, "jd");
        assertAccounts(datasource);
    }


    @Test
    void testFailInGettingConnectionWhenCreatingInitialSchema() throws Exception {
        DataSource datasource = mock(DataSource.class);
        when(datasource.getConnection()).thenThrow(new SQLException("Failed to get connection"));

        var runner = new SampleappProductionDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Failed to get connection");
    }


    @Test
    void testFailWhenCreatingInitialSchema() throws Exception {
        Connection connection = spy(createDataSource("sampleapp1").getConnection());
        DataSource datasource = mock(DataSource.class);
        when(datasource.getConnection()).thenReturn(connection);

        var runner = new SampleappProductionDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Failed to create schema in sampleapp PostgreSQL database");
    }

    @Test
    void testFailWhenAddingMockData() throws Exception {
        var connection = spy(createDataSource("sampleapp1").getConnection());
        DataSource datasource = spy(createDataSource("sampleapp2"));
        when(datasource.getConnection())
            .thenCallRealMethod()
            .thenReturn(connection);

        var runner = new SampleappProductionDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Failed to insert initial data in sampleapp PostgreSQL database");
    }


    @Test
    void testFailWhenGettingConnectionForUpdatingSchema() throws Exception {
        DataSource datasource = spy(createDataSource("sampleapp3"));
        when(datasource.getConnection())
            .thenCallRealMethod()
            .thenCallRealMethod()
            .thenThrow(new SQLException("Failed to get connection"));

        var runner = new SampleappProductionDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Failed to get connection");
    }


    @Test
    void testFailWhenUpdatingSchema() throws Exception {
        var connection = spy(createDataSource("sampleapp4").getConnection());
        DataSource datasource = spy(createDataSource("sampleapp4"));
        when(datasource.getConnection())
            .thenCallRealMethod()
            .thenCallRealMethod()
            .thenReturn(connection);

        var runner = new SampleappProductionDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Failed to modify schema of sampleapp PostgreSQL database");
    }


    private void assertAccounts(DataSource datasource) throws Exception {
        try (Connection connection = datasource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("select * from sampleapp_accounts")) {
                try (ResultSet results = statement.executeQuery()) {
                    assertAccount(results, "jd");
                }
            }
        }
    }

    private void assertAccount(ResultSet results, String username) throws Exception {
        assertTrue(results.next());
        assertEquals(username, results.getString(2)); // column 1 is the id
    }

    private int addAccount(DataSource datasource, String username) throws Exception {
        try (Connection connection = datasource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("insert into sampleapp_accounts (username) values (?)")) {
                statement.setString(1, username);
                statement.executeUpdate();
            }
        }
        int accountId = -1;
        try (Connection connection = datasource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("select * from sampleapp_accounts where username=?")) {
                statement.setString(1, username);
                try (ResultSet results = statement.executeQuery()) {
                    results.next();
                    accountId = results.getInt(1);
                }
            }
        }
        return accountId;
    }


    private DataSource createDataSource(String dbname) throws SQLException {
        DataSourceFactory dataSourceFactory = new DerbyDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:" + dbname + ";create=true");
        DataSource datasource = dataSourceFactory.createDataSource(properties);
        return datasource;
    }

}
