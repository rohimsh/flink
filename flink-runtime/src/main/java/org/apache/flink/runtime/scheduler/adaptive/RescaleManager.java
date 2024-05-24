/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.runtime.scheduler.adaptive;

import org.apache.flink.runtime.scheduler.adaptive.allocator.VertexParallelism;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/** The {@code RescaleManager} decides on whether rescaling should happen or not. */
public interface RescaleManager {

    /** Is called if the environment changed in a way that a rescaling could be considered. */
    void onChange();

    /**
     * The interface that can be used by the {@code }RescaleManager} to communicate with the
     * underlying system.
     */
    interface Context {

        /**
         * Returns the {@link VertexParallelism} of the currently deployed {@link
         * org.apache.flink.runtime.executiongraph.ExecutionGraph}.
         */
        VertexParallelism getCurrentVertexParallelism();

        /**
         * Returns the {@link VertexParallelism} that could be achieved by the currently available
         * free slots for the job or an empty {@code Optional} if there are not enough free slots.
         */
        Optional<VertexParallelism> getAvailableVertexParallelism();

        /** Triggers the rescaling of the job. */
        void rescale();

        /** Runs operation with a given delay in the underlying main thread. */
        void scheduleOperation(Runnable callback, Duration delay);
    }

    /** Interface for creating {@code RescaleManager} instances. */
    interface Factory {

        /**
         * Creates a {@code RescaleManager} instance for the given {@code rescaleContext} and
         * previous rescale time.
         */
        RescaleManager create(Context rescaleContext, Instant lastRescale);
    }
}
