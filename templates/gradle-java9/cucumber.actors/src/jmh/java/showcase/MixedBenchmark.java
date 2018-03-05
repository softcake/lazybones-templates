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

package showcase;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark with different {@link BenchmarkMode}s to show that its possible.
 */
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MixedBenchmark {

    @Benchmark
    @Warmup(iterations = 1)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 3)
    public void avg() throws InterruptedException {
        Thread.sleep(50);
    }

    @Benchmark
    @Warmup(iterations = 1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    public void throughput() throws InterruptedException {
        Thread.sleep(50);
    }

    @Benchmark
    @Warmup(iterations = 0)
    @BenchmarkMode(Mode.SingleShotTime)
    public void singleShot() throws InterruptedException {
        Thread.sleep(50);
    }

}
