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

import no.priv.bang.osgiservice.users.User;

public record Account(int accountId, User user) {

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {
        int accountId;
        User user;

        public Account build() {
            return new Account(this.accountId, this.user);
        }

        public Builder accountId(int accountId) {
            this.accountId = accountId;
            return this;
        }
        public Builder user(User user) {
            this.user = user;
            return this;
        }

    }

}
