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

public record Loginresult(boolean success, String errormessage, boolean authorized, User user, String originalRequestUrl) {

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String errormessage;
        private boolean authorized;
        private User user;
        private String originalRequestUrl;

        private Builder() {}

        public Loginresult build() {
            return new Loginresult(this.success, this.errormessage, authorized, user, originalRequestUrl);
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder errormessage(String errormessage) {
            this.errormessage = errormessage;
            return this;
        }

        public Builder authorized(boolean authorized) {
            this.authorized = authorized;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder originalRequestUrl(String originalRequestUrl) {
            this.originalRequestUrl = originalRequestUrl;
            return this;
        }
    }

}
