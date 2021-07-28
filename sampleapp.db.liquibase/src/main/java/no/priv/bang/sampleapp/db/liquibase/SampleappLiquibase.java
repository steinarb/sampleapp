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

import java.sql.Connection;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public class SampleappLiquibase {

    public void createInitialSchema(Connection connection) throws LiquibaseException {
        applyLiquibaseChangelist(connection, "sampleapp-db-changelog/db-changelog-1.0.0.xml");
    }

    public void updateSchema(Connection connection) throws LiquibaseException {
        applyLiquibaseChangelist(connection, "sampleapp-db-changelog/db-changelog-1.0.1.xml");
    }

    public void forceReleaseLocks(Connection connection) throws LiquibaseException {
        Liquibase liquibase = createLiquibaseInstance(connection, "sampleapp-db-changelog/db-changelog-1.0.0.xml");
        liquibase.forceReleaseLocks();
    }

    private void applyLiquibaseChangelist(Connection connection, String changelistClasspathResource) throws LiquibaseException {
        Liquibase liquibase = createLiquibaseInstance(connection, changelistClasspathResource);
        liquibase.update("");
    }

    private Liquibase createLiquibaseInstance(Connection connection, String changelistClasspathResource) throws LiquibaseException {
        DatabaseConnection databaseConnection = new JdbcConnection(connection);
        ClassLoaderResourceAccessor classLoaderResourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());
        return new Liquibase(changelistClasspathResource, classLoaderResourceAccessor, databaseConnection);
    }

}
