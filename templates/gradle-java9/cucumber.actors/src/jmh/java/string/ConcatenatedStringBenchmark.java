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

package string;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;


@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@CompilerControl(CompilerControl.Mode.EXCLUDE)
public class ConcatenatedStringBenchmark {

    private final String[] _strings = new String[]{"This", " is", " a", " concatenated", " String!"};

    private String validate(final String string) {

        assertEquals(string.length(), "This is a concatenated String!".length());
        return string;
    }

    @Benchmark
    public String stringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : _strings) {
            stringBuilder.append(string);
        }
        return validate(stringBuilder.toString());
    }

    @Benchmark
    public String stringBuffer() {
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : _strings) {
            stringBuffer.append(string);
        }
        return validate(stringBuffer.toString());
    }

    @Benchmark
    public String pluses() {
        StringBuilder result = new StringBuilder();
        for (String string : _strings) {
            result.append(string);
        }
        return validate(result.toString());
    }

    @Benchmark
    public String stringBuilderPreSized() {
        StringBuilder stringBuilder = new StringBuilder(30);
        for (String string : _strings) {
            stringBuilder.append(string);
        }
        return validate(stringBuilder.toString());
    }

    @Benchmark
    public String stringBufferPreSized() {
        StringBuffer stringBuffer = new StringBuffer(30);
        for (String string : _strings) {
            stringBuffer.append(string);
        }
        return validate(stringBuffer.toString());
    }

}
