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

import static no.priv.bang.sampleapp.services.SampleappConstants.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.log.Logger;

import no.priv.bang.sampleapp.services.Account;
import no.priv.bang.sampleapp.services.SampleappService;
import no.priv.bang.osgiservice.users.Role;
import no.priv.bang.osgiservice.users.User;
import no.priv.bang.osgiservice.users.UserManagementService;

@Component(service=SampleappService.class, immediate=true)
public class SampleappServiceProvider implements SampleappService {

    private Logger logger;
    private DataSource datasource;
    private UserManagementService useradmin;

    @Reference
    public void setLogservice(LogService logservice) {
        this.logger = logservice.getLogger(SampleappServiceProvider.class);
    }

    @Reference(target = "(osgi.jndi.service.name=jdbc/sampleapp)")
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    @Reference
    public void setUseradmin(UserManagementService useradmin) {
        this.useradmin = useradmin;
    }

    @Activate
    public void activate() {
        addRolesIfNotpresent();
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = datasource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet results = statement.executeQuery("select * from accounts")) {
                    while(results.next()) {
                        int accountId = results.getInt(1);
                        String username = results.getString(2);
                        User user = useradmin.getUser(username);
                        Account account = Account.with().accountId(accountId).user(user).build();
                        accounts.add(account);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Ingen sampleapp");
        }

        return accounts;
    }

    private void addRolesIfNotpresent() {
        String sampleappuser = SAMPLEAPPUSER_ROLE;
        List<Role> roles = useradmin.getRoles();
        Optional<Role> existingRole = roles.stream().filter(r -> sampleappuser.equals(r.getRolename())).findFirst();
        if (!existingRole.isPresent()) {
            useradmin.addRole(Role.with().id(-1).rolename(sampleappuser).description("Bruker av applikasjonen sampleapp").build());
        }
    }

}
