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
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
public class StringConstantBenchmark {

    private static final String STRING_SEPERATOR = ",";
    private static final char CHAR_SEPERATOR = ',';
    private final String[] _tokens = new String[]{"a", "b", "c", "d", "e", "f", "g"};

    private String validate(final String string) {
        assertEquals(string, "a,b,c,d,e,f,g,");
        return string;
    }

    @Benchmark
    public String buildStringWithSeperatorAsStringConstant() {
        StringBuilder builder = new StringBuilder();
        for (String string : _tokens) {
            builder.append(string).append(STRING_SEPERATOR);
        }
        return validate(builder.toString());
    }

    @Benchmark
    public String buildStringWithSeperatorAsCharConstant() {
        StringBuilder builder = new StringBuilder();
        for (String string : _tokens) {
            builder.append(string).append(CHAR_SEPERATOR);
        }
        return validate(builder.toString());
    }

    @Benchmark
    public String buildStringWithStringSeperator() {
        StringBuilder builder = new StringBuilder();
        for (String string : _tokens) {
            builder.append(string).append(",");
        }
        return validate(builder.toString());
    }

    @Benchmark
    public String buildStringWithCharSeperator() {
        StringBuilder builder = new StringBuilder();
        for (String string : _tokens) {
            builder.append(string).append(',');
        }
        return validate(builder.toString());
    }
}
