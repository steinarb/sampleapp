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

public class Loginresult {

    private boolean success;
    private String errormessage;
    private boolean authorized;
    private User user;
    private String originalRequestUrl;

    private Loginresult() {}

    public boolean getSuccess() {
        return success;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public User getUser() {
        return user;
    }

    public String getOriginalRequestUrl() {
        return originalRequestUrl;
    }

    @Override
    public String toString() {
        return "Loginresult [success=" + success + ", errormessage=" + errormessage + ", authorized=" + authorized + "originalRequestUrl=" + originalRequestUrl + "]";
    }

    public static LoginresultBuilder with() {
        return new LoginresultBuilder();
    }

    public static class LoginresultBuilder {
        private boolean success;
        private String errormessage;
        private boolean authorized;
        private User user;
        private String originalRequestUrl;

        private LoginresultBuilder() {}

        public Loginresult build() {
            var loginresult = new Loginresult();
            loginresult.success = this.success;
            loginresult.errormessage = this.errormessage;
            loginresult.authorized = authorized;
            loginresult.user = user;
            loginresult.originalRequestUrl = originalRequestUrl;
            return loginresult;
        }

        public LoginresultBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public LoginresultBuilder errormessage(String errormessage) {
            this.errormessage = errormessage;
            return this;
        }

        public LoginresultBuilder authorized(boolean authorized) {
            this.authorized = authorized;
            return this;
        }

        public LoginresultBuilder user(User user) {
            this.user = user;
            return this;
        }

        public LoginresultBuilder originalRequestUrl(String originalRequestUrl) {
            this.originalRequestUrl = originalRequestUrl;
            return this;
        }
    }

}
