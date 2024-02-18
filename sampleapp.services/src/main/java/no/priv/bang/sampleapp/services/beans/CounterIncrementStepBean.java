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

public class CounterIncrementStepBean extends Immutable {
    private String username;
    private Integer counterIncrementStep;

    private CounterIncrementStepBean() {}

    public static CounterIncrementStepBeanBuilder with() {
        return new CounterIncrementStepBeanBuilder();
    }

    public String getUsername() {
        return username;
    }

    public Integer getCounterIncrementStep() {
        return counterIncrementStep;
    }

    public static class CounterIncrementStepBeanBuilder {
        private String username;
        private int counterIncrementStep;

        private CounterIncrementStepBeanBuilder() {}

        public CounterIncrementStepBean build() {
            var counterIncrementStepBean = new CounterIncrementStepBean();
            counterIncrementStepBean.username = this.username;
            counterIncrementStepBean.counterIncrementStep = this.counterIncrementStep;
            return counterIncrementStepBean;
        }

        public CounterIncrementStepBeanBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CounterIncrementStepBeanBuilder counterIncrementStep(int counterIncrementStep) {
            this.counterIncrementStep = counterIncrementStep;
            return this;
        }

    }

}
