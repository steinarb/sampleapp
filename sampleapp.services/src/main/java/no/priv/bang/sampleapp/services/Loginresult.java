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

public class Loginresult {

    private boolean suksess;
    private String feilmelding;
    private boolean authorized;
    private String username;
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

    public String getUsername() {
        return username;
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
        private String username;
        private String originalRequestUrl;

        private LoginresultBuilder() {}

        public Loginresult build() {
            Loginresult loginresultat = new Loginresult();
            loginresultat.suksess = this.suksess;
            loginresultat.feilmelding = this.feilmelding;
            loginresultat.authorized = authorized;
            loginresultat.username = username;
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

        public LoginresultBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LoginresultBuilder originalRequestUrl(String originalRequestUrl) {
            this.originalRequestUrl = originalRequestUrl;
            return this;
        }
    }

}
