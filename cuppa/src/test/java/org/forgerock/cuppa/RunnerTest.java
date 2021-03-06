/*
 * Copyright 2016 ForgeRock AS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forgerock.cuppa;

import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.TestCuppaSupport.defineTests;
import static org.forgerock.cuppa.TestCuppaSupport.runTests;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.forgerock.cuppa.functions.TestFunction;
import org.forgerock.cuppa.model.TestBlock;
import org.forgerock.cuppa.reporters.Reporter;
import org.testng.annotations.Test;

public class RunnerTest {

    @Test
    public void shouldMarkTestsAsNotRunningAfterTestRun() throws Exception {

        //Given
        TestFunction function = mock(TestFunction.class);

        //When
        TestBlock emptyTestBlock = defineTests(() -> {
        });
        runTests(emptyTestBlock, mock(Reporter.class));
        TestBlock rootBlock = defineTests(() -> {
            describe("Second Test", () -> {
                it("runs the second test", function);
            });
        });
        runTests(rootBlock, mock(Reporter.class));

        //Then
        verify(function).apply();
    }
}
