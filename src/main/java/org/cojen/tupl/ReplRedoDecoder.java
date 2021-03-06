/*
 *  Copyright 2013-2015 Cojen.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.cojen.tupl;

import java.io.IOException;

import org.cojen.tupl.ext.ReplicationManager;

/**
 * 
 *
 * @author Brian S O'Neill
 * @see ReplRedoEngine
 */
final class ReplRedoDecoder extends RedoDecoder {
    private final In mIn;

    ReplRedoDecoder(ReplicationManager manager, long initialPosition, long initialTxnId) {
        super(false, initialTxnId);
        mIn = new In(initialPosition, manager);
    }

    @Override
    DataIn in() {
        return mIn;
    }

    @Override
    boolean verifyTerminator(DataIn in) {
        // No terminators to verify.
        return true;
    }

    static final class In extends DataIn {
        private final ReplicationManager mManager;

        In(long position, ReplicationManager manager) {
            this(position, manager, 4096);
        }

        In(long position, ReplicationManager manager, int bufferSize) {
            super(position, bufferSize);
            mManager = manager;
        }

        @Override
        int doRead(byte[] buf, int off, int len) throws IOException {
            return mManager.read(buf, off, len);
        }

        @Override
        public void close() throws IOException {
            // Nothing to close.
        }
    }
}
