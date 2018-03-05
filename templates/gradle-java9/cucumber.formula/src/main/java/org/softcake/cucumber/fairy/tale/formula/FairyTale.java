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

import org.softcake.cucumber.actors.api.Actor;
import org.softcake.cucumber.actors.api.Group;
import org.softcake.cucumber.fairy.tale.Tale;
import org.softcake.cucumber.fairy.tale.formula.events.Event;
import org.softcake.cucumber.fairy.tale.formula.events.IntransativeEvent;
import org.softcake.cucumber.fairy.tale.formula.events.TransitiveEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * .
 *
 * @author The softcake Authors.
 */
public final class FairyTale implements Tale {
    /**
     * The Separator.
     */
    public static final String LINE_SEPARATOR = "line.separator";
    private final List<Actor> actors;
    private final List<Event> events;

    private FairyTale(final List<Actor> actors, final List<Event> events) {
        this.actors = actors;
        this.events = events;
    }

    /**
     * get the Weaver.
     *
     * @return the weaver.
     */
    public static Weaver getWeaver() {
        return new Weaver(new HashSet<>(), new HashSet<>(), new ArrayList<>());
    }

    @Override
    public void tell() {
        StringBuilder builder = new StringBuilder("Once upon a time, there lived ");
        for (int i = 0; i < actors.size(); i++) {
            if (i == actors.size() - 1 && i != 0) {
                builder.append("and ");
            }
            builder.append(actors.get(i));
            if (i != actors.size() - 1 && actors.size() > 1) {
                builder.append(", ");
            }
        }
        builder.append(".").append(System.getProperty(LINE_SEPARATOR));

        for (Event event : events) {
            builder.append(event).append(System.getProperty(LINE_SEPARATOR));
        }
        builder.append("And they all lived happily ever after.")
               .append(System.getProperty(LINE_SEPARATOR))
               .append(System.getProperty(LINE_SEPARATOR));
        System.out.print(builder.toString());
    }

    /**
     * The Weaver class.
     *
     * @author The softcake Authors.
     */
    public static final class Weaver {
        private final Set<Actor> actorSet;
        private final Set<Actor> groupActors;
        private final List<Event> events;

        private Weaver(final Set<Actor> actorSet, final Set<Actor> groupActors, final List<Event> events) {
            this.actorSet = actorSet;
            this.groupActors = groupActors;
            this.events = events;
        }

        /**
         * weave.
         *
         * @return weave.
         */
        public Tale weave() {
            List<Actor> actors = new ArrayList<>(actorSet);
            return new FairyTale(actors, events);
        }

        /**
         * record.
         *
         * @param actor  the actor
         * @param action the action
         * @return the weaver
         */
        public Weaver record(final Actor actor, final String action) {
            addActorOrGroup(actor);
            events.add(new IntransativeEvent(actor, action));
            return this;
        }

        /**
         * record.
         *
         * @param actor the actor
         * @param action the action
         * @param object the object
         * @return the record
         */
        public Weaver record(final Actor actor, final String action, final Actor object) {
            addActorOrGroup(actor);
            addActorOrGroup(object);
            events.add(new TransitiveEvent(actor, action, object));
            return this;
        }

        private void addActorOrGroup(final Actor actor) {
            if (actor instanceof Group) {
                for (Actor a : (Group) actor) {
                    actorSet.remove(a);
                    groupActors.add(a);
                }
            }
            if (!groupActors.contains(actor)) {
                actorSet.add(actor);
            }
        }
    }
}
