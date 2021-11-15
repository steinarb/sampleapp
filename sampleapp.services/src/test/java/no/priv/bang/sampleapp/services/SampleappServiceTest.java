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
package no.priv.bang.sampleapp.services;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import no.priv.bang.sampleapp.services.beans.Account;
import no.priv.bang.sampleapp.services.beans.CounterBean;
import no.priv.bang.sampleapp.services.beans.CounterIncrementStepBean;
import no.priv.bang.sampleapp.services.beans.LocaleBean;

class SampleappServiceTest {

    @Test
    void testOfAllOfTheMethods() {
        SampleappService service = mock(SampleappService.class);
        String username = "jad";
        boolean created = service.lazilyCreateAccount(username);
        assertFalse(created);
        List<Account> accounts = service.getAccounts();
        assertThat(accounts).isEmpty();
        Optional<CounterIncrementStepBean> incrementStep = service.getCounterIncrementStep(username);
        assertTrue(incrementStep.isEmpty());
        Optional<CounterIncrementStepBean> updatedStep = service.updateCounterIncrementStep(CounterIncrementStepBean.with().build());
        assertTrue(updatedStep.isEmpty());
        Optional<CounterBean> counter = service.getCounter(username);
        assertTrue(counter.isEmpty());
        Optional<CounterBean> incrementedCounter = service.incrementCounter(username);
        assertTrue(incrementedCounter.isEmpty());
        Optional<CounterBean> decrementedCounter = service.decrementCounter(username);
        assertTrue(decrementedCounter.isEmpty());
        Locale defaultLocale = service.defaultLocale();
        assertNull(defaultLocale);
        List<LocaleBean> availableLocales = service.availableLocales();
        assertThat(availableLocales).isEmpty();
        Locale locale = Locale.UK;
        Map<String, String> texts = service.displayTexts(locale);
        assertThat(texts).isEmpty();
        String key = "loggedout";
        String text = service.displayText(key, locale.toString());
        assertNull(text);
    }

}
