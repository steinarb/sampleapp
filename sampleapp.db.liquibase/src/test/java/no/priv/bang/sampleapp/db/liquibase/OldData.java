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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OldData {
    final List<String> nærbutikker = Arrays.asList(
            "Meny Stovner Senter",
            "COOP Prix Vestli",
            "RIMI Stovner Senter",
            "Sultan Marked Stovner",
            "Rema 1000 Rommen",
            "Rema1000 Haugenstua",
            "ICA Linderud",
            "COOP Mega Metrosenteret",
            "OBS Triaden",
            "Kiwi Lørenskog",
            "Bunnpris Holumporten",
            "Kiwi Stovner");
    final List<String> deaktivert = Arrays.asList(
            "Ultra Stovner Senter",
            "Annet");
    List<String> butikker = new ArrayList<>();
    List<Handling> handlinger = new ArrayList<>();

    public OldData() throws Exception {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("olddata/butikker.txt"), StandardCharsets.ISO_8859_1))) {
            while(reader.ready()) {
                String line = reader.readLine();
                butikker.add(line);
            }
        }

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("olddata/handel.txt"), StandardCharsets.ISO_8859_1))) {
            while(reader.ready()) {
                String line = reader.readLine();
                String[] fields = line.split("\t");
                String username = fields[0];
                Date timestamp = new Date(Long.parseLong(fields[1]) * 1000);
                double belop = Double.parseDouble(fields[2]);
                String butikk = fields[3];
                Handling handling = new Handling(username, timestamp, belop, butikk);
                handlinger.add(handling);
            }
        }
    }


}
