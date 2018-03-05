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

package experimental;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * Benchmarking different ways coupling a primitive reader with a primitive consumer.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class SwitchVsPolymorphismBenchmark {

    private IntConsumer _intConsumer = new IntConsumer();
    private PolymorphicIntReader _polymorphicReader = new PolymorphicIntReader();
    private SwitchIntReader _switchReader = new SwitchIntReader();
    private CoupledIntReader _coupledReader = new CoupledIntReader(_intConsumer);

    @Benchmark
    public int polymorphic() {
        _polymorphicReader.read(_intConsumer);
        return _intConsumer.getCurrent();
    }

    @Benchmark
    public int switchStyle() {
        _switchReader.read(_intConsumer);
        return _intConsumer.getCurrent();
    }

    @Benchmark
    public int coupled() {
        _coupledReader.read();
        return _intConsumer.getCurrent();
    }

    public static abstract class PolymorphicReader {

        public void read(Consumer consumer) {
            throw new UnsupportedOperationException("Does not support consumer class " + consumer.getClass());
        }

        public void read(IntConsumer consumer) {
            throw new UnsupportedOperationException();
        }

        public void read(StringConsumer consumer) {
            throw new UnsupportedOperationException();
        }

    }

    public static class PolymorphicIntReader extends PolymorphicReader {

        private int _current;

        @Override
        public void read(IntConsumer consumer) {
            consumer.consume(_current++);
        }

    }

    public enum Type {
        INT, STRING
    }

    public static abstract class SwitchReader {

        final void read(Consumer consumer) {
            switch (consumer.getType()) {
            case INT:
                readInt((IntConsumer) consumer);
                break;
            case STRING:
                readString((StringConsumer) consumer);
                break;
            default:
                throw new UnsupportedOperationException("Does not support consumer of type " + consumer.getType());
            }
        }

        protected void readInt(IntConsumer consumer) {
            throw new UnsupportedOperationException();
        }

        void readString(StringConsumer consumer) {
            throw new UnsupportedOperationException();
        }

    }

    public static class SwitchIntReader extends SwitchReader {

        private int _current;

        @Override
        public void readInt(IntConsumer consumer) {
            consumer.consume(_current++);
        }

    }

    public static abstract class CoupledReader {

        public abstract void read();

    }

    public static class CoupledIntReader extends CoupledReader {

        private int _current;
        private final IntConsumer _consumer;

        CoupledIntReader(IntConsumer consumer) {
            _consumer = consumer;
        }

        @Override
        public void read() {
            _consumer.consume(_current++);

        }

    }

    public interface Consumer {
        Type getType();
    }

    public static class IntConsumer implements Consumer {

        private int _current;

        int getCurrent() {
            return _current;
        }

        @Override
        public Type getType() {
            return Type.INT;
        }

        void consume(int value) {
            _current = value;
        }
    }

    public static class StringConsumer implements Consumer {

        private String _current;

        public String getCurrent() {
            return _current;
        }

        @Override
        public Type getType() {
            return Type.STRING;
        }

        void consume(String value) {
            _current = value;
        }
    }
}
