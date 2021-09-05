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
package no.priv.bang.sampleapp.db.liquibase;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

class SampleappLiquibaseTest {
    DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();

    @Test
    void testCreateSchema() throws Exception {
        Connection connection = createConnection();
        SampleappLiquibase sampleappLiquibase = new SampleappLiquibase();
        sampleappLiquibase.createInitialSchema(connection);
        addAccounts(connection);
        assertAccounts(connection);
        sampleappLiquibase.updateSchema(connection);
    }

    @Test
    void testForceReleaseLocks() throws Exception {
        Connection connection = createConnection();
        SampleappLiquibase sampleappLiquibase = new SampleappLiquibase();
        assertDoesNotThrow(() -> sampleappLiquibase.forceReleaseLocks(connection));
    }

    private void addAccounts(Connection connection) throws Exception {
        addAccount(connection, "admin");
    }

    private void assertAccounts(Connection connection) throws Exception {
        String sql = "select count(*) from sampleapp_accounts";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    int count = results.getInt(1);
                    assertEquals(1, count);
                }
            }
        }
    }

    private int addAccount(Connection connection, String username) throws Exception {
        String sql = "insert into sampleapp_accounts (username) values (?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }

        return findAccountId(connection, username);
    }

    private int findAccountId(Connection connection, String username) throws Exception {
        String sql = "select account_id from sampleapp_accounts where username=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private Connection createConnection() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:sampleapp;create=true");
        DataSource dataSource = derbyDataSourceFactory.createDataSource(properties);
        return dataSource.getConnection();
    }

}
