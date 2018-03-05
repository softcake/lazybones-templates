/*
 * Copyright 2018 softcake.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.softcake.cucumber.actors.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import org.junit.jupiter.api.Test;


/**
 * .
 * @author The softcake Authors.
 */
public class GroupTest {
    private static final String NOBODY = "nobodies";
    private static final Group EMPTY = Imagination.createGroup(NOBODY);

    private static final String BAND_NAME = "Beatles";
    private static final Actor JOHN = Imagination.createActor("John Lennon");
    private static final Actor PAUL = Imagination.createActor("Paul McCartney");
    private static final Actor GEORGE = Imagination.createActor("George Harrison");
    private static final Actor RINGO = Imagination.createActor("Ringo Starr");

    @Test
    public void nameMatches() {
        assertEquals(NOBODY, EMPTY.name());
    }

    @Test
    public void noActorsByDefault() {
        assertTrue(EMPTY.isEmpty());
    }

    @Test
    public void containsActors() {
        Group beatles = Imagination.createGroup(BAND_NAME, JOHN, PAUL, GEORGE, RINGO);
        
        assertEquals(4, beatles.size());
        assertTrue(beatles.contains(JOHN));
        assertTrue(beatles.contains(PAUL));
        assertTrue(beatles.contains(GEORGE));
        assertTrue(beatles.contains(RINGO));
    }

    @Test
    public void accurateToString() {
        Group beatles = Imagination.createGroup(BAND_NAME, JOHN, PAUL, GEORGE, RINGO);
        assertEquals("the 4 Beatles", beatles.toString());
        assertEquals("the 0 nobodies", EMPTY.toString());
    }
}
