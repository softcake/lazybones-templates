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

package org.softcake.cucumber.actors.api.impl;

import org.softcake.cucumber.actors.api.Actor;
import org.softcake.cucumber.actors.api.Group;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Iterator;

/**
 * Default Implementation of {@link Group}.
 *
 * @author The softcake Authors.
 */
public class DefaultGroup implements Group {
    private final String name;
    private final ImmutableSet<Actor> actors;

    /**
     * .
     *
     * @param name the Name
     * @param actors the acors
     */
    public DefaultGroup(final String name, final ImmutableSet<Actor> actors) {
        super();
        this.name = name;
        this.actors = actors;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("the %d %s", size(), name());
    }

    @Override
    public boolean isEmpty() {
        return actors.isEmpty();
    }

    @Override
    public boolean contains(final Object obj) {
        return actors.contains(obj);
    }

    @Override
    public Object[] toArray() {
        return actors.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return actors.toArray(a);
    }

    @Override
    @Deprecated
    public boolean add(final Actor actor) {
        return actors.add(actor);
    }

    @Override
    @Deprecated
    public boolean remove(final Object o) {
        return actors.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return actors.containsAll(c);
    }

    @Override
    @Deprecated
    public boolean addAll(final Collection<? extends Actor> c) {
        return actors.addAll(c);
    }

    @Override
    @Deprecated
    public boolean retainAll(final Collection<?> c) {
        return actors.retainAll(c);
    }

    @Override
    @Deprecated
    public boolean removeAll(final Collection<?> c) {
        return actors.removeAll(c);
    }

    @Override
    @Deprecated
    public void clear() {
        actors.clear();
    }

    @Override
    public int size() {
        return actors.size();
    }

    @Override
    public Iterator<Actor> iterator() {
        return actors.iterator();
    }
}
