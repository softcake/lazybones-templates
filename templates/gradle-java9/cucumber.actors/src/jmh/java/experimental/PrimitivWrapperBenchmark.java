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

import com.google.common.collect.ImmutableList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Benchmarking different approaches access primitive values (flexible schema).
 */
@Fork(value = 2)
@Warmup(iterations = 10)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class PrimitivWrapperBenchmark {

    public static final String DOPPELPUNKT = ": ";

    private final ImmutableList<PrimitivHolder>
        holders
        = ImmutableList.<PrimitivHolder>builder().add(new PrimitivStringHolder())
                                                 .add(new PrimitivLongHolder())
                                                 .add(new PrimitivBooleanHolder())
                                                 .add(new PrimitivStringHolder())
                                                 .add(new PrimitivBooleanHolder()).build();

    private final ValueBase _valueBase = new ValueBase(holders);
    private long _currentRow;

    @Benchmark
    public void cast1(final Blackhole blackhole) {
        blackhole.consume(_valueBase.getString_Cast1(0, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast1(1, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast1(2, _currentRow));
        blackhole.consume(_valueBase.getString_Cast1(3, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast1(4, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast1(5, _currentRow));
        _currentRow++;
    }

    @Benchmark
    public void cast2(final Blackhole blackhole) {
        blackhole.consume(_valueBase.getString_Cast2(0, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast2(1, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast2(2, _currentRow));
        blackhole.consume(_valueBase.getString_Cast2(3, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast2(4, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast2(5, _currentRow));
        _currentRow++;
    }

    @Benchmark
    public void cast3(final Blackhole blackhole) {
        blackhole.consume(_valueBase.getString_Cast3(0, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast3(1, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast3(2, _currentRow));
        blackhole.consume(_valueBase.getString_Cast3(3, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast3(4, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast3(5, _currentRow));
        _currentRow++;
    }

    @Benchmark
    public void extractor(final Blackhole blackhole) {
        blackhole.consume(_valueBase.getString_Extract(0, _currentRow));
        blackhole.consume(_valueBase.getLong_Extract(1, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Extract(2, _currentRow));
        blackhole.consume(_valueBase.getString_Extract(3, _currentRow));
        blackhole.consume(_valueBase.getLong_Extract(4, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Extract(5, _currentRow));
        _currentRow++;
    }

    private interface PrimitivHolder {
        // marker interface
    }

    private static final class ValueBase {

        private final List<PrimitivHolder> _primitivHolders;
        private final PrimitivExtractor[] _primitivExtractors;

        private ValueBase(final List<PrimitivHolder> primitivHolders) {
            _primitivHolders = primitivHolders;
            _primitivExtractors = new PrimitivExtractor[primitivHolders.size()];
            for (int i = 0; i < _primitivExtractors.length; i++) {
                _primitivExtractors[i] = PrimitivExtractor.create(primitivHolders.get(i), i);
            }
        }

        public String getString_Cast1(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (primitivHolder instanceof PrimitivStringHolder) {
                return ((PrimitivStringHolder) primitivHolder).getString(row);
            }
            throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                .getClass().getSimpleName());
        }

        public long getLong_Cast1(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (primitivHolder instanceof PrimitivLongHolder) {
                return ((PrimitivLongHolder) primitivHolder).getLong(row);
            }
            throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                .getClass().getSimpleName());
        }

        public boolean getBoolean_Cast1(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (primitivHolder instanceof PrimitivBooleanHolder) {
                return ((PrimitivBooleanHolder) primitivHolder).getBoolean(row);
            }
            throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                .getClass().getSimpleName());
        }

        public String getString_Cast2(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (PrimitivStringHolder.class.isInstance(primitivHolder)) {
                return PrimitivStringHolder.class.cast(primitivHolder).getString(row);
            }
            throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                .getClass().getSimpleName());
        }

        public long getLong_Cast2(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (PrimitivLongHolder.class.isInstance(primitivHolder)) {
                return PrimitivLongHolder.class.cast(primitivHolder).getLong(row);
            }
            throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                .getClass().getSimpleName());
        }

        public boolean getBoolean_Cast2(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (PrimitivBooleanHolder.class.isInstance(primitivHolder)) {
                return PrimitivBooleanHolder.class.cast(primitivHolder).getBoolean(row);
            }
            throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                .getClass().getSimpleName());
        }

        public String getString_Cast3(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            try {
                return PrimitivStringHolder.class.cast(primitivHolder).getString(row);
            } catch (final ClassCastException e) {
                throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                    .getClass().getSimpleName());
            }
        }

        public long getLong_Cast3(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            try {
                return PrimitivLongHolder.class.cast(primitivHolder).getLong(row);
            } catch (final ClassCastException e) {
                throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                    .getClass().getSimpleName());
            }
        }

        public boolean getBoolean_Cast3(final int columnIndex, final long row) {
            final PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            try {
                return PrimitivBooleanHolder.class.cast(primitivHolder).getBoolean(row);
            } catch (final ClassCastException e) {
                throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                    .getClass().getSimpleName());
            }
        }

        public String getString_Extract(final int columnIndex, final long row) {
            return _primitivExtractors[columnIndex].getString(row);
        }

        public long getLong_Extract(final int columnIndex, final long row) {
            return _primitivExtractors[columnIndex].getLong(row);
        }

        public boolean getBoolean_Extract(final int columnIndex, final long row) {
            return _primitivExtractors[columnIndex].getBoolean(row);
        }

    }

    private static class PrimitivStringHolder implements PrimitivHolder {
        public String getString(final long row) {
            return "string" + row;
        }
    }

    private static class PrimitivLongHolder implements PrimitivHolder {
        public long getLong(final long row) {
            return row;
        }
    }

    private static class PrimitivBooleanHolder implements PrimitivHolder {
        public boolean getBoolean(final long row) {
            return row % 2 == 0;
        }
    }

    private static class PrimitivExtractor {

        private final PrimitivHolder _primitivHolder;
        private final int _columnIndex;

        private PrimitivExtractor(final PrimitivHolder primitivHolder, final int columnIndex) {
            _primitivHolder = primitivHolder;
            _columnIndex = columnIndex;
        }

        public static PrimitivExtractor create(final PrimitivHolder primitivHolder,
            final int columnIndex) {
            if (primitivHolder instanceof PrimitivStringHolder) {
                return new PrimitivStringExtractor((PrimitivStringHolder) primitivHolder,
                                                   columnIndex);
            } else if (primitivHolder instanceof PrimitivLongHolder) {
                return new PrimitivLongExtractor((PrimitivLongHolder) primitivHolder, columnIndex);
            } else if (primitivHolder instanceof PrimitivBooleanHolder) {
                return new PrimitivBooleanExtractor((PrimitivBooleanHolder) primitivHolder,
                                                    columnIndex);
            }
            throw new UnsupportedOperationException(columnIndex + DOPPELPUNKT + primitivHolder
                .getClass().getSimpleName());
        }

        public String getString(final long row) {
            throw new UnsupportedOperationException(_columnIndex + DOPPELPUNKT + _primitivHolder
                .getClass().getSimpleName());
        }

        public long getLong(final long row) {
            throw new UnsupportedOperationException(_columnIndex + DOPPELPUNKT + _primitivHolder
                .getClass().getSimpleName());
        }

        public boolean getBoolean(final long row) {
            throw new UnsupportedOperationException(_columnIndex + DOPPELPUNKT + _primitivHolder
                .getClass().getSimpleName());
        }
    }

    private static final class PrimitivStringExtractor extends PrimitivExtractor {

        private final PrimitivStringHolder _primitivHolder;

        private PrimitivStringExtractor(final PrimitivStringHolder primitivHolder,
            final int columnIndex) {
            super(primitivHolder, columnIndex);
            _primitivHolder = primitivHolder;
        }

        @Override
        public String getString(final long row) {
            return _primitivHolder.getString(row);
        }

    }

    private static final class PrimitivLongExtractor extends PrimitivExtractor {

        private final PrimitivLongHolder _primitivHolder;

        private PrimitivLongExtractor(final PrimitivLongHolder primitivHolder,
            final int columnIndex) {
            super(primitivHolder, columnIndex);
            _primitivHolder = primitivHolder;
        }

        @Override
        public long getLong(final long row) {
            return _primitivHolder.getLong(row);
        }
    }

    private static final class PrimitivBooleanExtractor extends PrimitivExtractor {

        private final PrimitivBooleanHolder _primitivHolder;

        private PrimitivBooleanExtractor(final PrimitivBooleanHolder primitivHolder,
            final int columnIndex) {
            super(primitivHolder, columnIndex);
            _primitivHolder = primitivHolder;
        }

        @Override
        public boolean getBoolean(final long row) {
            return _primitivHolder.getBoolean(row);
        }
    }
}
