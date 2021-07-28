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
package no.priv.bang.sampleapp.db.liquibase;

import java.util.Date;

public class Handling {

    String username;
    Date timestamp;
    double belop;
    String butikk;

    public Handling(String username, Date timestamp, double belop, String butikk) {
        this.username = username;
        this.timestamp = timestamp;
        this.belop = belop;
        this.butikk = butikk;
    }

    @Override
    public String toString() {
        return "Handling [username=" + username + ", timestamp=" + timestamp + ", belop=" + belop + ", butikk=" + butikk + "]";
    }

}
