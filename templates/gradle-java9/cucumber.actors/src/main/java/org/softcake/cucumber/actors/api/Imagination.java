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

import org.softcake.cucumber.actors.api.impl.DefaultActor;
import org.softcake.cucumber.actors.api.impl.DefaultGroup;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * The place where {@link Actor}s and {@link Group}s come from.
 */
public final class Imagination {
    private Imagination() {
    }

    /**
     * Creates and returns an {@link Actor} with the given <code>name</code>.
     *
     * @param name the Name
     */
    public static Actor createActor(final String name) {

        Preconditions.checkNotNull(name);
        return new DefaultActor(name);
    }

    /**
     * Creates a {@link Group} with the given <code>name</code> containing the <code>actors</code>
     *
     * @param name the Name
     * @param actors the Actors
     */
    public static Group createGroup(final String name, final Actor... actors) {
        return new DefaultGroup(name, ImmutableSet.copyOf(actors));
    }
}
