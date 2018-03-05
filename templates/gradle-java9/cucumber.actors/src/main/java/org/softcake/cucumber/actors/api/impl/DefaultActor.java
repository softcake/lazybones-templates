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

import com.google.common.base.Preconditions;

/**
 * .
 * Default implementation of {@link Actor}
 *
 * @author The softcake Authors.
 */
public class DefaultActor implements Actor {
    private final String lastName;
    private String name;
    private String sex;

    /**
     * Constructor.
     *
     * @param name the name
     */
    public DefaultActor(final String name) {

        this(name, "");

    }

    /**
     * Constructor.
     *
     * @param name     the firstname
     * @param lastName the lastname
     */
    public DefaultActor(final String name, final String lastName) {
        Preconditions.checkNotNull(name);
        this.name = name;
        this.lastName = lastName;
    }

    /**
     * Constructor.
     *
     * @return a string
     */
    public String getLastName() {
        return lastName;
    }

    @Override
    public String name() {
        return name;
    }


    @Override
    public String toString() {
        return name();
    }

    /**
     * Sets the firstname.
     *
     * @param name the firstname
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get the sex.
     *
     * @return the sex string
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the sex.
     *
     * @param sex the sex
     */
    public void setSex(final String sex) {
        this.sex = sex;
    }
}
