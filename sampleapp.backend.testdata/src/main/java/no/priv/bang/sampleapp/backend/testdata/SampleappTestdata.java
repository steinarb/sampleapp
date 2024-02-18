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
package no.priv.bang.sampleapp.backend.testdata;

import static no.priv.bang.sampleapp.services.SampleappConstants.*;

import java.util.Arrays;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import no.priv.bang.sampleapp.services.SampleappService;
import no.priv.bang.osgiservice.users.UserManagementService;
import no.priv.bang.osgiservice.users.UserRoles;

@Component(immediate=true)
public class SampleappTestdata {

    private UserManagementService useradmin;

    @Reference
    public void setSampleappService(SampleappService sampleapp) {
        // Brukes bare til å bestemme rekkefølge på kjøring
        // Når denne blir kalt vet vi at authservice har
        // rollen sampleappuser lagt til
    }

    @Reference
    public void setUseradmin(UserManagementService useradmin) {
        this.useradmin = useradmin;
    }

    @Activate
    public void activate() {
        addRolesForTestusers();
    }

    void addRolesForTestusers() {
        var sampleappuser = useradmin.getRoles().stream().filter(r -> SAMPLEAPPUSER_ROLE.equals(r.getRolename())).findFirst().get(); // NOSONAR testkode
        var jad = useradmin.getUser("jad");
        useradmin.addUserRoles(UserRoles.with().user(jad).roles(Arrays.asList(sampleappuser)).build());
    }

}
