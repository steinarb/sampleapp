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
package no.priv.bang.sampleapp.db.liquibase;

import static liquibase.Scope.Attr.database;
import static liquibase.Scope.Attr.resourceAccessor;
import static liquibase.command.core.UpdateCommandStep.CHANGELOG_FILE_ARG;
import static liquibase.command.core.helpers.DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS;
import static liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep.DATABASE_ARG;

import java.sql.Connection;
import java.util.Map;

import liquibase.Scope;
import liquibase.changelog.ChangeLogParameters;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
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

    public void applyLiquibaseChangelist(Connection connection, String changelistClasspathResource, ClassLoader classLoader) throws LiquibaseException {
        try (var liquibaseDatabase = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))) {
            Scope.child(scopeObjects(classLoader, liquibaseDatabase), () -> {
                    new CommandScope("update")
                        .addArgumentValue(DATABASE_ARG, liquibaseDatabase)
                        .addArgumentValue(CHANGELOG_FILE_ARG, changelistClasspathResource)
                        .addArgumentValue(CHANGELOG_PARAMETERS, new ChangeLogParameters(liquibaseDatabase))
                        .execute();
                });
        } catch (LiquibaseException e) {
            throw e;
        } catch (Exception e) {
            // AutoClosable.close() may throw Exception
            throw new LiquibaseException(e);
        }
    }

    private Map<String, Object> scopeObjects(ClassLoader classLoader, Database liquibaseDatabase) {
        return Map.of(
            database.name(), liquibaseDatabase,
            resourceAccessor.name(), new ClassLoaderResourceAccessor(classLoader));
    }

    private void applyLiquibaseChangelist(Connection connection, String changelistClasspathResource) throws LiquibaseException {
        applyLiquibaseChangelist(connection, changelistClasspathResource, getClass().getClassLoader());
    }

}
