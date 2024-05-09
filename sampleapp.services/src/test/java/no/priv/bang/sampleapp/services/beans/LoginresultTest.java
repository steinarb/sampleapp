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
package no.priv.bang.sampleapp.services.beans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import no.priv.bang.osgiservice.users.User;

class LoginresultTest {

    @Test
    void testCreate() {
        var success = true;
        var authorized = true;
        var errormessage = "Feil passord";
        var originalRequestUrl = "http://localhost:8181/sampleapp/hurtigregistrering";
        var username = "jod";
        var user = User.with().username(username).build();
        var bean = Loginresult.with()
            .success(success)
            .errormessage(errormessage)
            .authorized(authorized)
            .user(user)
            .originalRequestUrl(originalRequestUrl)
            .build();
        assertTrue(bean.success());
        assertEquals(errormessage, bean.errormessage());
        assertTrue(bean.authorized());
        assertEquals(username, bean.user().username());
        assertEquals(originalRequestUrl, bean.originalRequestUrl());
    }

    @Test
    void testNoargsConstructor() {
        var bean = Loginresult.with().build();
        assertFalse(bean.success());
        assertNull(bean.errormessage());
        assertFalse(bean.authorized());
        assertNull(bean.originalRequestUrl());
    }

    @Test
    void testToString() {
        var bean = Loginresult.with().build();
        assertThat(bean.toString()).startsWith("Loginresult[");
    }

}
