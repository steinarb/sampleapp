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

public class Credentials {

    private String username;
    private String password;

    private Credentials() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Credentials [username=" + username + ", password=" + password + "]";
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {
        private String username;
        private String password;

        private Builder() {}

        public Credentials build() {
            var credentials = new Credentials();
            credentials.username = this.username;
            credentials.password = this.password;
            return credentials;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }
    }

}
