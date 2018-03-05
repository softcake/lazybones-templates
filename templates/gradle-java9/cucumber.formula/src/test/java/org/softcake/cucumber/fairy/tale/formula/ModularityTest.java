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

package org.softcake.cucumber.fairy.tale.formula;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.softcake.cucumber.actors.api.Actor;
import org.softcake.cucumber.actors.api.Imagination;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests the visibility of objects to the formula module.
 */
@SuppressWarnings("unchecked")
public class ModularityTest {
    @Test
    public void canReachActor() {
        Actor actor = Imagination.createActor("Sean Connery");
        assertEquals("Sean Connery", actor.toString());
    }

    @Test
   @Disabled("Java 9 modularity even stops reflection.")
    public void canDynamicallyReachDefaultActor() throws Exception {
        Class clazz = ModularityTest
            .class.getClassLoader()
                  .loadClass("org.softcake.cucumber.actors.api.impl.DefaultActor");
        Actor actor = (Actor) clazz.getConstructor(String.class)
                                   .newInstance("Kevin Costner");
        assertEquals("Kevin Costner", actor.toString());
    }

    /*@Test
    public void canReachDefaultActor() {
        // With Java 9 modules, this line now fails to compile.
        Actor actor = new DefaultActor("Kevin Costner");
        assertEquals("Kevin Costner", actor.toString());
    }

    @Test
    public void canReachGuavaClasses() {
        // This line would throw a compiler error because gradle has kept the implementation dependency "guava"
        // from leaking into the formula project.
        Set<String> strings = com.google.common.collect.ImmutableSet.of("Hello", "Goodbye");
        assertTrue(strings.contains("Hello"));
        assertTrue(strings.contains("Goodbye"));
    }*/
}
