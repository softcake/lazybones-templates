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

package org.softcake.cucumber.documentation.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.softcake.lemon.core.tester.PrivateConstructorTester;
import org.softcake.lemon.core.tester.utils.TestUtil;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author softcake.org
 */
public class DemoTest {

    // tag::exampleReflection[]
    @Test
    public void constructorMustBePrivateAndThrowException_Old() {

        try {
            Constructor<?>[] constructors = Demo.class.getDeclaredConstructors();
            Constructor<?> constructor = constructors[0];

            constructor.setAccessible(true);
            constructor.newInstance();

            fail("constructor must throw exception");

        } catch (final InvocationTargetException expected) {

            IllegalStateException cause = (IllegalStateException) expected.getCause();
            assertEquals("No instances!", cause.getMessage());

        } catch (InstantiationException | IllegalAccessException e) {

            fail("constructor must throw IllegalStateException");
        }
    }
    // end::exampleReflection[]

    // tag::exampleLemon1[]
    @Test
    public void constructorMustBePrivateAndThrowException_New() {

        PrivateConstructorTester
                .forClass(Demo.class)
                .expectedExceptionType(IllegalStateException.class, "No instances!")
                .check();
    }
    // end::exampleLemon1[]

    // tag::exampleLemon2[]
    @Test
    public void constructorMustBePrivateAndThrowException() {

        String nestedClassName = "NestedClass";
        Object enclosingInstance = TestUtil.getEnclosingInstance(EnclosingClass.class,
                                                                         new Object[]{},
                                                                         new Class[]{});
                Class<?> nestedClass = TestUtil.forNestedName(nestedClassName, enclosingInstance);

        PrivateConstructorTester
                .forClass(nestedClass, enclosingInstance)
                .expectedExceptionType(IllegalStateException.class, "No instances!")
                .check();
    }
    // end::exampleLemon2[]
}
