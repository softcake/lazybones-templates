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

package datastructure;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * You know the eclipse warning 'Access to enclosing ... is emulated by a synthetic accessor method" ? This benchmark
 * tries to measure the effect on performance to it.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class SyntheticAccessorBenchmark {

    private final ClassWithPrivateMethod _classWithPrivateMethod = new ClassWithPrivateMethod();
    private final ClassWithProtectedMethod _classWithProtectedMethod = new ClassWithProtectedMethod();

    @Benchmark
    @SuppressWarnings("synthetic-access")
    public int emulate() {
        return _classWithPrivateMethod.nextInt();
    }

    @Benchmark
    public int dontEmulate() {
        return _classWithProtectedMethod.nextInt();
    }

    protected static class ClassWithPrivateMethod {

        private final Random _random = new Random(23);

        private int nextInt() {
            return _random.nextInt();
        }
    }

    protected static class ClassWithProtectedMethod {

        private final Random _random = new Random(23);

        protected int nextInt() {
            return _random.nextInt();
        }
    }

}
