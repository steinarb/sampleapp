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

import no.priv.bang.osgiservice.users.User;

public class Loginresult {

    private boolean suksess;
    private String feilmelding;
    private boolean authorized;
    public User user;
    private String originalRequestUrl;

    private Loginresult() {}

    public boolean getSuksess() {
        return suksess;
    }

    public String getFeilmelding() {
        return feilmelding;
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
        return "Loginresultat [suksess=" + suksess + ", feilmelding=" + feilmelding + ", authorized=" + authorized + "originalRequestUrl=" + originalRequestUrl + "]";
    }

    public static LoginresultBuilder with() {
        return new LoginresultBuilder();
    }

    public static class LoginresultBuilder {
        private boolean suksess;
        private String feilmelding;
        private boolean authorized;
        private User user;
        private String originalRequestUrl;

        private LoginresultBuilder() {}

        public Loginresult build() {
            Loginresult loginresultat = new Loginresult();
            loginresultat.suksess = this.suksess;
            loginresultat.feilmelding = this.feilmelding;
            loginresultat.authorized = authorized;
            loginresultat.user = user;
            loginresultat.originalRequestUrl = originalRequestUrl;
            return loginresultat;
        }

        public LoginresultBuilder suksess(boolean suksess) {
            this.suksess = suksess;
            return this;
        }

        public LoginresultBuilder feilmelding(String feilmelding) {
            this.feilmelding = feilmelding;
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
