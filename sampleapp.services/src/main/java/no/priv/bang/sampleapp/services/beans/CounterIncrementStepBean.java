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

public record CounterIncrementStepBean(String username, Integer counterIncrementStep) {

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {
        private String username;
        private int counterIncrementStep;

        private Builder() {}

        public CounterIncrementStepBean build() {
            return new CounterIncrementStepBean(this.username, this.counterIncrementStep);
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder counterIncrementStep(int counterIncrementStep) {
            this.counterIncrementStep = counterIncrementStep;
            return this;
        }

    }

}
