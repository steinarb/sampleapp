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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import no.priv.bang.sampleapp.services.beans.Account;
import no.priv.bang.sampleapp.services.beans.CounterBean;
import no.priv.bang.sampleapp.services.beans.CounterIncrementStepBean;
import no.priv.bang.sampleapp.services.beans.LocaleBean;

public interface SampleappService {

    public List<Account> getAccounts();

    public Optional<CounterIncrementStepBean> getCounterIncrementStep(String username);

    public Optional<CounterIncrementStepBean> updateCounterIncrementStep(CounterIncrementStepBean cupdatedIncrementStep);

    public Optional<CounterBean> getCounter(String username);

    public Optional<CounterBean> incrementCounter(String username);

    public Optional<CounterBean> decrementCounter(String username);

    Locale defaultLocale();

    List<LocaleBean> availableLocales();

    public Map<String, String> displayTexts(Locale locale);

    public String displayText(String key, String locale);

    public boolean lazilyCreateAccount(String username);

}
