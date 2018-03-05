/*
 *
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

package actors;

import org.junit.jupiter.api.Test;
import org.softcake.cucumber.actors.api.Actor;
import org.softcake.cucumber.actors.api.Imagination;
import org.softcake.cucumber.actors.api.impl.DefaultActor;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ActorIntegTest {
    private static final String NAME = "Sean Connery";
    private static final Actor SEAN = Imagination.createActor(NAME);
    private final int hello = 1;

    @Test
    public void nameMatches() {
        assertEquals(NAME, SEAN.name());
    }

    @Test
    public void accurateToString() {
        assertEquals(NAME, SEAN.toString());
    }

    @Test
    public void canReachDefaultActor() {
        Actor actor = new DefaultActor("Kevin Costner");
        assertEquals("Kevin Costner", actor.toString());
    }

    @Test
    public void setNameActor() {
        DefaultActor actor = new DefaultActor("Kevin Costner");
        actor.setName("Hans");
        assertEquals("Hans", actor.toString());
    }

}
