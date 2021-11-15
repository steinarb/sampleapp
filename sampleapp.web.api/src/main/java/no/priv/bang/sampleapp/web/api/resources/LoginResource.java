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

import static no.priv.bang.sampleapp.services.SampleappConstants.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.osgi.service.log.LogService;
import org.osgi.service.log.Logger;

import no.priv.bang.authservice.definitions.AuthserviceException;
import no.priv.bang.osgiservice.users.User;
import no.priv.bang.osgiservice.users.UserManagementService;
import no.priv.bang.sampleapp.services.SampleappService;
import no.priv.bang.sampleapp.services.beans.Credentials;
import no.priv.bang.sampleapp.services.beans.Loginresult;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private Logger logger;

    @Context
    HttpServletRequest request;

    @Inject
    SampleappService sampleapp;

    @Inject
    UserManagementService useradmin;

    @Inject
    void setLogservice(LogService logservice) {
        this.logger = logservice.getLogger(LoginResource.class);
    }

    @POST
    @Path("/login")
    public Loginresult login(@QueryParam("locale")String locale, Credentials credentials) {
        Subject subject = SecurityUtils.getSubject();
        String username = credentials.getUsername();

        UsernamePasswordToken token = new UsernamePasswordToken(username, credentials.getPassword().toCharArray(), true);
        try {
            subject.login(token);
            String originalRequestUrl = findOriginalRequestUrl();
            boolean authorized = subject.hasRole(SAMPLEAPPUSER_ROLE);
            if (authorized) {
                sampleapp.lazilyCreateAccount(username);
            }

            User user = useradmin.getUser(username);

            return Loginresult.with()
                .success(true)
                .errormessage("")
                .authorized(authorized)
                .user(user)
                .originalRequestUrl(originalRequestUrl)
                .build();
        } catch(UnknownAccountException e) {
            logger.warn("Login error: unknown account", e);
            return Loginresult.with().success(false).errormessage(sampleapp.displayText("unknownaccount", locale)).build();
        } catch (IncorrectCredentialsException  e) {
            logger.warn("Login error: wrong password", e);
            return Loginresult.with().success(false).errormessage(sampleapp.displayText("wrongpassword", locale)).build();
        } catch (LockedAccountException  e) {
            logger.warn("Login error: locked account", e);
            return Loginresult.with().success(false).errormessage(sampleapp.displayText("lockedaccount", locale)).build();
        } catch (AuthenticationException e) {
            logger.warn("Login error: general authentication error", e);
            return Loginresult.with().success(false).errormessage(sampleapp.displayText("unknownerror", locale)).build();
        } catch (Exception e) {
            logger.error("Login error: internal server error", e);
            throw new InternalServerErrorException();
        } finally {
            token.clear();
        }
    }

    @GET
    @Path("/logout")
    public Loginresult logout(@QueryParam("locale")String locale) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return Loginresult.with()
            .success(false)
            .errormessage(sampleapp.displayText("loggedout", locale))
            .user(User.with().build())
            .build();
    }

    @GET
    @Path("/loginstate")
    public Loginresult loginstate(@QueryParam("locale")String locale) {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        boolean success = subject.isAuthenticated();
        boolean harRoleSampleappuser = subject.hasRole(SAMPLEAPPUSER_ROLE);
        String brukerLoggetInnMelding = harRoleSampleappuser ?
            sampleapp.displayText("userloggedinwithaccesses", locale) :
            sampleapp.displayText("userloggedinwithoutaccesses", locale);
        String melding = success ? brukerLoggetInnMelding : sampleapp.displayText("usernotloggedin", locale);
        User user = findUserSafely(username);
        return Loginresult.with()
            .success(success)
            .errormessage(melding)
            .authorized(harRoleSampleappuser)
            .user(user)
            .build();
    }

    User findUserSafely(String username) {
        try {
            return useradmin.getUser(username);
        } catch (AuthserviceException e) {
            return User.with().build();
        }
    }

    String findOriginalRequestUrl() {
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        String contextPath = request.getContextPath();
        String originalRequestUrl = savedRequest != null ? savedRequest.getRequestUrl() : null;
        if (contextPath != null && originalRequestUrl != null) {
            return originalRequestUrl.replaceFirst(contextPath, "");
        }

        return originalRequestUrl;
    }

}
