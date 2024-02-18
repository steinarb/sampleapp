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

import no.priv.bang.beans.immutable.Immutable;

public class CounterBean extends Immutable {
    private Integer counter;

    private CounterBean() {}

    public static CounterBeanBuilder with() {
        return new CounterBeanBuilder();
    }

    public Integer getCounter() {
        return counter;
    }

    public static class CounterBeanBuilder {
        private int counter;

        private CounterBeanBuilder() {}

        public CounterBean build() {
            var counterBean = new CounterBean();
            counterBean.counter = counter;
            return counterBean;
        }

        public CounterBeanBuilder counter(int counter) {
            this.counter = counter;
            return this;
        }

    }

}
