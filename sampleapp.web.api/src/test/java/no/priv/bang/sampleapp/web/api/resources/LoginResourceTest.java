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
package no.priv.bang.sampleapp.web.api.resources;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.subject.WebSubject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import no.priv.bang.sampleapp.services.Credentials;
import no.priv.bang.sampleapp.services.Loginresult;
import no.priv.bang.sampleapp.web.api.ShiroTestBase;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;

class LoginResourceTest extends ShiroTestBase {

    @Test
    void testLogin() {
        LoginResource resource = new LoginResource();
        String username = "jd";
        String password = "johnnyBoi";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        Loginresult resultat = resource.login(credentials);
        assertTrue(resultat.getSuksess());
    }

    @Test
    void testLoginFeilPassord() {
        MockLogService logservice = new MockLogService();
        LoginResource resource = new LoginResource();
        resource.setLogservice(logservice);
        String username = "jd";
        String password = "feil";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        Loginresult resultat = resource.login(credentials);
        assertFalse(resultat.getSuksess());
        assertThat(resultat.getFeilmelding()).startsWith("Feil passord");
    }

    @Test
    void testLoginUkjentBrukernavn() {
        MockLogService logservice = new MockLogService();
        LoginResource resource = new LoginResource();
        resource.setLogservice(logservice);
        String username = "jdd";
        String password = "feil";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        Loginresult resultat = resource.login(credentials);
        assertThat(resultat.getFeilmelding()).startsWith("Ukjent konto");
    }

    @Test
    void testLogout() {
        LoginResource resource = new LoginResource();
        String username = "jd";
        String password = "johnnyBoi";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);
        assertTrue(subject.isAuthenticated()); // Verify precondition user logged in

        Loginresult loginresultat = resource.logout();
        assertFalse(loginresultat.getSuksess());
        assertEquals("Logget ut", loginresultat.getFeilmelding());
        assertFalse(loginresultat.isAuthorized());
        assertFalse(subject.isAuthenticated()); // Verify user has been logged out
    }

    @Test
    void testGetLoginstateWhenLoggedIn() {
        LoginResource resource = new LoginResource();
        String username = "jd";
        String password = "johnnyBoi";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);

        Loginresult loginresultat = resource.loginstate();
        assertTrue(loginresultat.getSuksess());
        assertEquals("Bruker er logget inn og har tilgang", loginresultat.getFeilmelding());
        assertTrue(loginresultat.isAuthorized());
    }

    @Test
    void testGetLoginstateWhenLoggedInButUserDoesntHaveRoleSampleappuser() {
        LoginResource resource = new LoginResource();
        String username = "jad";
        String password = "1ad";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);

        Loginresult loginresultat = resource.loginstate();
        assertTrue(loginresultat.getSuksess());
        assertEquals("Bruker er logget inn men mangler tilgang", loginresultat.getFeilmelding());
        assertFalse(loginresultat.isAuthorized());
    }

    @Test
    void testGetLoginstateWhenNotLoggedIn() {
        LoginResource resource = new LoginResource();
        createSubjectAndBindItToThread();

        Loginresult loginresultat = resource.loginstate();
        assertFalse(loginresultat.getSuksess());
        assertEquals("Bruker er ikke logget inn", loginresultat.getFeilmelding());
        assertFalse(loginresultat.isAuthorized());
    }

}
