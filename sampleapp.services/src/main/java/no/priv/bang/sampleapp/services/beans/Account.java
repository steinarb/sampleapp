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

import no.priv.bang.beans.immutable.Immutable;
import no.priv.bang.osgiservice.users.User;

public class Account extends Immutable {
    int accountId;
    User user;

    public int getAccountId() {
        return accountId;
    }
    public User getUser() {
        return user;
    }

    public static AccountBuilder with() {
        return new AccountBuilder();
    }

    public static class AccountBuilder {
        int accountId;
        User user;

        public Account build() {
            Account account = new Account();
            account.accountId = this.accountId;
            account.user = this.user;
            return account;
        }

        public AccountBuilder accountId(int accountId) {
            this.accountId = accountId;
            return this;
        }
        public AccountBuilder user(User user) {
            this.user = user;
            return this;
        }

    }

}
