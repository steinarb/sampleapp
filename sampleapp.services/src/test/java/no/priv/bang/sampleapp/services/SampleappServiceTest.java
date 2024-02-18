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
package no.priv.bang.sampleapp.services;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import org.junit.jupiter.api.Test;

import no.priv.bang.sampleapp.services.beans.CounterIncrementStepBean;

class SampleappServiceTest {

    @Test
    void testOfAllOfTheMethods() {
        var service = mock(SampleappService.class);
        var username = "jad";
        var created = service.lazilyCreateAccount(username);
        assertFalse(created);
        var accounts = service.getAccounts();
        assertThat(accounts).isEmpty();
        var incrementStep = service.getCounterIncrementStep(username);
        assertTrue(incrementStep.isEmpty());
        var updatedStep = service.updateCounterIncrementStep(CounterIncrementStepBean.with().build());
        assertTrue(updatedStep.isEmpty());
        var counter = service.getCounter(username);
        assertTrue(counter.isEmpty());
        var incrementedCounter = service.incrementCounter(username);
        assertTrue(incrementedCounter.isEmpty());
        var decrementedCounter = service.decrementCounter(username);
        assertTrue(decrementedCounter.isEmpty());
        var defaultLocale = service.defaultLocale();
        assertNull(defaultLocale);
        var availableLocales = service.availableLocales();
        assertThat(availableLocales).isEmpty();
        var locale = Locale.UK;
        var texts = service.displayTexts(locale);
        assertThat(texts).isEmpty();
        var key = "loggedout";
        var text = service.displayText(key, locale.toString());
        assertNull(text);
    }

}
