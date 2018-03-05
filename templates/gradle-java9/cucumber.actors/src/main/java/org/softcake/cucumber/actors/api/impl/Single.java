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

/**
 * .
 * @author René Neubert
 */
public final class Single {

    private Single() {
        throw new IllegalStateException("No instances!");
    }

    /**
     * Lazy  initialization, where the instance is created when this static method is first invoked.
     *
     * @return the instance of this Singleton class.
     */
    public static Single getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static final class InstanceHolder {
        private static final Single INSTANCE = new Single();
    }
}
