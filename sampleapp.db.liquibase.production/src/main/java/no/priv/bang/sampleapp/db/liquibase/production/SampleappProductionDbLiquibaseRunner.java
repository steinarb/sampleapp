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

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.ops4j.pax.jdbc.hook.PreHook;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.log.Logger;

import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import no.priv.bang.sampleapp.db.liquibase.SampleappLiquibase;

@Component(immediate=true, property = "name=sampleappdb")
public class SampleappProductionDbLiquibaseRunner implements PreHook {

    private Logger logger;

    @Reference
    public void setLogService(LogService logservice) {
        this.logger = logservice.getLogger(SampleappProductionDbLiquibaseRunner.class);
    }

    @Activate
    public void activate() {
        // Called after all injections have been satisfied and before the PreHook service is exposed
    }

    @Override
    public void prepare(DataSource datasource) throws SQLException {
        SampleappLiquibase sampleappLiquibase = new SampleappLiquibase();
        try (Connection connect = datasource.getConnection()) {
            sampleappLiquibase.createInitialSchema(connect);
        } catch (Exception e) {
            logError("Failed to create schema sampleapp PostgreSQL database", e);
        }

        try (Connection connect = datasource.getConnection()) {
            insertInitialData(connect);
        } catch (Exception e) {
            logError("Failed to insert initial data in sampleapp PostgreSQL database", e);
        }

        try (Connection connect = datasource.getConnection()) {
            sampleappLiquibase.updateSchema(connect);
        } catch (Exception e) {
            logError("Failed to modify schema of sampleapp PostgreSQL database", e);
        }
    }

    public void insertInitialData(Connection connect) throws Exception {
        DatabaseConnection databaseConnection = new JdbcConnection(connect);
        try(var classLoaderResourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader())) {
            try(var liquibase = new Liquibase("sql/data/db-changelog.xml", classLoaderResourceAccessor, databaseConnection)) {
                liquibase.update("");
            }
        }
    }

    private void logError(String message, Exception exception) {
        logger.error(message, exception);
    }

}
