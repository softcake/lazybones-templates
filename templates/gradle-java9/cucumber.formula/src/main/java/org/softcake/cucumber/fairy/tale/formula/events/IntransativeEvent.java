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

package org.softcake.cucumber.fairy.tale.formula.events;

import org.softcake.cucumber.actors.api.Actor;

/**
 * Something that some {@link Actor} did.
 */
public class IntransativeEvent implements Event {
    private final Actor actor;
    private final String action;

    public IntransativeEvent(final Actor actor, final String action) {
        this.actor = actor;
        this.action = action;
    }

    @Override
    public String toString() {
        return actor + " " + action;
    }
}
