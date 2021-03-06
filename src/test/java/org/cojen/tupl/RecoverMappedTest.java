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

import org.junit.*;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class RecoverMappedTest extends RecoverTest {
    public static void main(String[] args) throws Exception {
        org.junit.runner.JUnitCore.main(RecoverMappedTest.class.getName());
    }

    @Before
    @Override
    public void createTempDb() throws Exception {
        mConfig = new DatabaseConfig()
            .directPageAccess(false)
            .checkpointRate(-1, null)
            .durabilityMode(DurabilityMode.NO_FLUSH)
            .mapDataFiles(true);
        mDb = TestUtils.newTempDatabase(mConfig);
    }

    @Test
    @Override
    public void largeUndo() throws Exception {
        if (TestUtils.is64bit()) {
            super.largeUndo();
        }
    }

    @Test
    @Override
    public void largeUndoExit() throws Exception {
        if (TestUtils.is64bit()) {
            super.largeUndoExit();
        }
    }

    @Test
    @Override
    public void largeRedo() throws Exception {
        if (TestUtils.is64bit()) {
            super.largeRedo();
        }
    }

    @Test
    @Override
    public void largeRedoExit() throws Exception {
        if (TestUtils.is64bit()) {
            super.largeRedoExit();
        }
    }
}
