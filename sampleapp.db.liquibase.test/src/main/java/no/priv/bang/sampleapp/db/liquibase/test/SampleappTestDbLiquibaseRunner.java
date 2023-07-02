/*
 * Copyright 2021-2023 Steinar Bang
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
package no.priv.bang.sampleapp.db.liquibase.test;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.ops4j.pax.jdbc.hook.PreHook;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import liquibase.Scope;
import liquibase.ThreadLocalScopeManager;
import liquibase.exception.LiquibaseException;
import no.priv.bang.sampleapp.db.liquibase.SampleappLiquibase;

@Component(immediate=true, property = "name=sampleappdb")
public class SampleappTestDbLiquibaseRunner implements PreHook {

    @Activate
    public void activate() {
        // Called after all injections have been satisfied and before the PreHook service is exposed
        Scope.setScopeManager(new ThreadLocalScopeManager());
    }

    @Override
    public void prepare(DataSource datasource) throws SQLException {
        var sampleappLiquibase = new SampleappLiquibase();
        try (var connect = datasource.getConnection()) {
            sampleappLiquibase.createInitialSchema(connect);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Error creating sampleapp test database schema", e);
        }

        try (var connect = datasource.getConnection()) {
            insertMockData(connect, sampleappLiquibase);
        } catch (Exception e) {
            throw new SQLException("Error inserting sampleapp test database mock data", e);
        }

        try (var connect = datasource.getConnection()) {
            sampleappLiquibase.updateSchema(connect);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Error updating sampleapp test database schema", e);
        }
    }

    public void insertMockData(Connection connect, SampleappLiquibase sampleappLiquibase) throws LiquibaseException {
        sampleappLiquibase.applyLiquibaseChangelist(connect, "sql/data/db-changelog.xml", getClass().getClassLoader());
    }

}
