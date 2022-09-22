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

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import no.priv.bang.sampleapp.services.SampleappService;
import no.priv.bang.sampleapp.services.beans.CounterBean;
import no.priv.bang.sampleapp.services.beans.CounterIncrementStepBean;

@Path("counter")
@Produces(MediaType.APPLICATION_JSON)
public class CounterResource {

    @Inject
    SampleappService sampleapp;

    @GET
    @Path("incrementstep/{username}")
    public CounterIncrementStepBean getCounterIncrementStep(@PathParam("username") String username) {
        throwForbiddenIfNotLoggedInUser(username);
        return sampleapp
            .getCounterIncrementStep(username)
            .orElseThrow(NotFoundException::new);
    }

    @POST
    @Path("incrementstep")
    @Consumes(MediaType.APPLICATION_JSON)
    public CounterIncrementStepBean updateCounterIncrementStep(CounterIncrementStepBean updateIncrementStep) {
        throwForbiddenIfNotLoggedInUser(updateIncrementStep.getUsername());
        return sampleapp
            .updateCounterIncrementStep(updateIncrementStep)
            .orElseThrow(InternalServerErrorException::new);
    }

    @GET
    @Path("{username}")
    public CounterBean getCounter(@PathParam("username") String username) {
        throwForbiddenIfNotLoggedInUser(username);
        return sampleapp
            .getCounter(username)
            .orElseThrow(NotFoundException::new);
    }

    @GET
    @Path("{username}/increment")
    public CounterBean incrementCounter(@PathParam("username") String username) {
        throwForbiddenIfNotLoggedInUser(username);
        return sampleapp
            .incrementCounter(username)
            .orElseThrow(InternalServerErrorException::new);
    }

    @GET
    @Path("{username}/decrement")
    public CounterBean decrementCounter(@PathParam("username") String username) {
        throwForbiddenIfNotLoggedInUser(username);
        return sampleapp
            .decrementCounter(username)
            .orElseThrow(InternalServerErrorException::new);
    }

    private void throwForbiddenIfNotLoggedInUser(String username) {
        Subject subject = SecurityUtils.getSubject();
        String loggedInUsername = (String) subject.getPrincipal();
        if (username.isEmpty() || !loggedInUsername.equals(username)) {
            throw new ForbiddenException();
        }
    }

}
