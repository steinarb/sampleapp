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
package no.priv.bang.sampleapp.web.api.resources;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.priv.bang.sampleapp.services.SampleappService;
import no.priv.bang.sampleapp.services.beans.CounterBean;
import no.priv.bang.sampleapp.services.beans.CounterIncrementStepBean;
import no.priv.bang.sampleapp.web.api.ShiroTestBase;

class CounterResourceTest extends ShiroTestBase {

    @BeforeEach
    void loginUser() {
        loginUser("jad", "1ad");
    }

    @Test
    void testGetCounterIncrementStep() {
        var incrementStepValue = 1;
        var sampleapp = mock(SampleappService.class);
        var optionalIncrementStep = Optional.of(CounterIncrementStepBean.with().counterIncrementStep(incrementStepValue).build());
        when(sampleapp.getCounterIncrementStep(anyString())).thenReturn(optionalIncrementStep);
        var resource = new CounterResource();
        resource.sampleapp = sampleapp;
        var username = "jad";
        var bean = resource.getCounterIncrementStep(username);
        assertNotNull(bean);
        assertEquals(incrementStepValue, bean.counterIncrementStep());
    }

    @Test
    void testGetCounterIncrementStepWhenNotFound() {
        var sampleapp = mock(SampleappService.class);
        var resource = new CounterResource();
        resource.sampleapp = sampleapp;
        var username = "jad";
        assertThrows(NotFoundException.class, () -> resource.getCounterIncrementStep(username));
    }

    @Test
    void testPostCounterIncrementStep() {
        var incrementStepValue = 2;
        var sampleapp = mock(SampleappService.class);
        var optionalIncrementStep = Optional.of(CounterIncrementStepBean.with().counterIncrementStep(incrementStepValue).build());
        when(sampleapp.updateCounterIncrementStep(any())).thenReturn(optionalIncrementStep);
        var resource = new CounterResource();
        resource.sampleapp = sampleapp;
        var username = "jad";
        var updateIncrementStep = CounterIncrementStepBean.with()
            .username(username)
            .counterIncrementStep(incrementStepValue)
            .build();
        var bean = resource.updateCounterIncrementStep(updateIncrementStep);
        assertNotNull(bean);
        assertEquals(incrementStepValue, bean.counterIncrementStep());
    }

    @Test
    void testGetCounter() {
        var counterValue = 3;
        var sampleapp = mock(SampleappService.class);
        var counter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.getCounter(anyString())).thenReturn(counter);
        var resource = new CounterResource();
        resource.sampleapp = sampleapp;
        var username = "jad";
        var bean = resource.getCounter(username);
        assertNotNull(bean);
        assertEquals(counterValue, bean.getCounter());
    }

    @Test
    void testGetCounterWhenNotFound() {
        var sampleapp = mock(SampleappService.class);
        var resource = new CounterResource();
        resource.sampleapp = sampleapp;
        var username = "jad";
        assertThrows(NotFoundException.class, () -> resource.getCounter(username));
    }

    @Test
    void testIncrementCounter() {
        var counterValue = 3;
        var sampleapp = mock(SampleappService.class);
        var counter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.incrementCounter(anyString())).thenReturn(counter);
        var resource = new CounterResource();
        resource.sampleapp = sampleapp;
        var username = "jad";
        var bean = resource.incrementCounter(username);
        assertNotNull(bean);
        assertEquals(counterValue, bean.getCounter());
    }

    @Test
    void testDecrementCounter() {
        var counterValue = 3;
        var sampleapp = mock(SampleappService.class);
        var counter = Optional.of(CounterBean.with().counter(counterValue).build());
        when(sampleapp.decrementCounter(anyString())).thenReturn(counter);
        var resource = new CounterResource();
        resource.sampleapp = sampleapp;
        var username = "jad";
        var bean = resource.decrementCounter(username);
        assertNotNull(bean);
        assertEquals(counterValue, bean.getCounter());
    }

}
