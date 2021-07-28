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
import java.io.PrintWriter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.jupiter.api.Disabled;
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

    @Disabled("Pseudo-test that imports legacy data and turns them into SQL files that can be imported into an SQL database")
    @Test
    void createSqlFromOriginalData() throws Exception {
        Connection connection = createConnection();
        SampleappLiquibase sampleappLiquibase = new SampleappLiquibase();
        sampleappLiquibase.createInitialSchema(connection);
        OldData oldData = new OldData();
        assertEquals(137, oldData.butikker.size());
        assertEquals(4501, oldData.handlinger.size());
        Integer jdAccountid = addAccount(connection, "sb");
        Integer jadAccountid = addAccount(connection, "tlf");
        int nærbutikkRekkefølge = 0;
        int annenbutikkRekkefølge = 0;
        int gruppe = 1;
        int rekkefølge = 0;
        for (String store : oldData.butikker) {
            boolean deaktivert = oldData.deaktivert.contains(store);
            if (oldData.nærbutikker.contains(store)) {
                gruppe = 1;
                rekkefølge = (nærbutikkRekkefølge += 10);
            } else {
                gruppe = 2;
                rekkefølge = (annenbutikkRekkefølge += 10);
            }
            addStore(connection, store, gruppe, rekkefølge, deaktivert);
        }

        Map<String, Integer> accountids = new HashMap<>();
        accountids.put("jd", jdAccountid);
        accountids.put("jad", jadAccountid);
        try(PrintWriter storeWriter = new PrintWriter("accounts.sql")) {
            storeWriter.println("--liquibase formatted sql");
            storeWriter.println("--changeset sb:example_accounts");
            try(PreparedStatement statement = connection.prepareStatement("select username from accounts order by account_id")) {
                ResultSet results = statement.executeQuery();
                while(results.next()) {
                    String username = results.getString(1);
                    storeWriter.println(String.format("insert into accounts (username) values ('%s');", username));
                }
            }
        }

        Map<String, Integer> storeids = findStoreIds(connection);
        assertEquals(137, storeids.size());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try(PrintWriter transactionWriter = new PrintWriter("transactions.sql")) {
            transactionWriter.println("--liquibase formatted sql");
            transactionWriter.println("--changeset sb:example_transactions");
            for (Handling handling : oldData.handlinger) {
                int accountid = accountids.get(handling.username);
                System.out.println("handling: " + handling);
                int storeid = storeids.get(handling.butikk);
                double belop = handling.belop;
                String timestamp = format.format(handling.timestamp);
                transactionWriter.println(String.format("insert into transactions (account_id, store_id, transaction_time, transaction_amount) values (%d, %d, '%s', %f);", accountid, storeid, timestamp, belop));
            }
        }
    }

    private void addAccounts(Connection connection) throws Exception {
        addAccount(connection, "admin");
    }

    private void assertAccounts(Connection connection) throws Exception {
        String sql = "select count(*) from accounts";
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
        String sql = "insert into accounts (username) values (?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }

        return findAccountId(connection, username);
    }

    private int findAccountId(Connection connection, String username) throws Exception {
        String sql = "select account_id from accounts where username=?";
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

    private Map<String, Integer> findStoreIds(Connection connection) throws Exception {
        Map<String, Integer> storeids = new HashMap<>();
        try(PrintWriter storeWriter = new PrintWriter("stores.sql")) {
            storeWriter.println("--liquibase formatted sql");
            storeWriter.println("--changeset sb:example_stores");
            try(PreparedStatement statement = connection.prepareStatement("select * from stores")) {
                ResultSet results = statement.executeQuery();
                while(results.next()) {
                    String storename = results.getString(2);
                    Integer storeid = results.getInt(1);
                    Integer gruppe = results.getInt(3);
                    Integer rekkefølge = results.getInt(4);
                    boolean deaktivert = results.getBoolean(5);
                    storeids.put(storename, storeid);
                    storeWriter.println(String.format("insert into stores (store_name, gruppe, rekkefolge, deaktivert) values ('%s', %d, %d, %b);", storename, gruppe, rekkefølge, deaktivert));
                }
            }
        }

        return storeids;
    }

    private void addStore(Connection connection, String storename, int gruppe, int rekkefølge, boolean deaktivert) throws Exception {
        try(PreparedStatement statement = connection.prepareStatement("insert into stores (store_name, gruppe, rekkefolge, deaktivert) values (?, ?, ?, ?)")) {
            statement.setString(1, storename);
            statement.setInt(2, gruppe);
            statement.setInt(3, rekkefølge);
            statement.setBoolean(4, deaktivert);
            statement.executeUpdate();
        }
    }

    private Connection createConnection() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:sampleapp;create=true");
        DataSource dataSource = derbyDataSourceFactory.createDataSource(properties);
        return dataSource.getConnection();
    }

}
