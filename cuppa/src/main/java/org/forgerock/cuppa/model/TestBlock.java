/*
 * Copyright 2015-2016 ForgeRock AS.
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

package org.forgerock.cuppa.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.forgerock.cuppa.functions.TestBlockFunction;

/**
 * Models a collection of tests and/or nested test blocks.
 *
 * <p>A {@code TestBlock} is usually created by calling
 * {@link org.forgerock.cuppa.Cuppa#describe(String, TestBlockFunction)} or
 * {@link org.forgerock.cuppa.Cuppa#when(String, TestBlockFunction)} but can be constructed using
 * {@link TestBlockBuilder}.</p>
 *
 * <p>{@code TestBlock}s form a tree, which together contain all the tests defined in the program. There is always a
 * single root {@code TestBlock}, which has the type {@link TestBlockType#ROOT}.</p>
 *
 * <p>For example, the following test code</p>
 *
 * <pre>
 *     describe("a", () -&gt; {
 *         beforeEach("a-be1", () -&gt; {
 *
 *         });
 *
 *         it("a1");
 *
 *         describe("b", () -&gt; {
 *             it("b1");
 *             it("b3");
 *         });
 *     });
 * </pre>
 *
 * <p>would result in a TestBlock tree of</p>
 *
 * <pre>
 *      root (TestBlock)
 *       |
 *       +-- a (TestBlock)
 *           |
 *           +-- a1 (Test)
 *           +-- a-be1 (Hook)
 *           +-- b (TestBlock)
 *               |
 *               +-- b1 (Test)
 *               +-- b2 (Test)
 * </pre>
 *
 */
public final class TestBlock {

    /**
     * The type of the test block.
     */
    public final TestBlockType type;

    /**
     * Controls how the test block and its descendants behave.
     */
    public final Behaviour behaviour;

    /**
     * The class that the test block was defined in.
     */
    public final Class<?> testClass;

    /**
     * The description of the test block. Will be used for reporting.
     */
    public final String description;

    /**
     * Nested test blocks.
     */
    public final List<TestBlock> testBlocks;

    /**
     * Hooks defined by the test block.
     *
     * <p>This list does not contain hooks from any of the nested blocks.</p>
     */
    public final List<Hook> hooks;

    /**
     * Tests defined by the test block.
     *
     * <p>This list does not contain tests from any of the nested blocks.</p>
     */
    public final List<Test> tests;

    /**
     * The set of options applied to the test block.
     */
    public final Options options;

    // Package private. Use TestBlockBuilder.
    TestBlock(TestBlockType type, Behaviour behaviour, Class<?> testClass, String description,
            List<TestBlock> testBlocks, List<Hook> hooks, List<Test> tests, Options options) {
        Objects.requireNonNull(type, "TestBlock must have a type");
        Objects.requireNonNull(behaviour, "TestBlock must have a behaviour");
        Objects.requireNonNull(testClass, "TestBlock must have a testClass");
        Objects.requireNonNull(description, "TestBlock must have a description");
        Objects.requireNonNull(testBlocks, "TestBlock must have testBlocks");
        Objects.requireNonNull(hooks, "TestBlock must have hooks");
        Objects.requireNonNull(tests, "TestBlock must have tests");
        Objects.requireNonNull(options, "TestBlock must have options");
        this.type = type;
        this.behaviour = behaviour;
        this.testClass = testClass;
        this.description = description;
        this.testBlocks = Collections.unmodifiableList(new ArrayList<>(testBlocks));
        this.hooks = Collections.unmodifiableList(new ArrayList<>(hooks));
        this.tests = Collections.unmodifiableList(new ArrayList<>(tests));
        this.options = options;
    }

    /**
     * Creates a {@link TestBlockBuilder} and initialises it's properties to this {@code TestBlock}.
     * @return a {@link TestBlockBuilder}.
     */
    public TestBlockBuilder toBuilder() {
        return new TestBlockBuilder()
                .setType(type)
                .setBehaviour(behaviour)
                .setTestClass(testClass)
                .setDescription(description)
                .setTestBlocks(new ArrayList<>(testBlocks))
                .setHooks(new ArrayList<>(hooks))
                .setTests(new ArrayList<>(tests))
                .setOptions(options);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestBlock testBlock = (TestBlock) o;

        return Objects.equals(type, testBlock.type)
            && Objects.equals(behaviour, testBlock.behaviour)
            && Objects.equals(testClass, testBlock.testClass)
            && Objects.equals(description, testBlock.description)
            && Objects.equals(testBlocks, testBlock.testBlocks)
            && Objects.equals(hooks, testBlock.hooks)
            && Objects.equals(tests, testBlock.tests)
            && Objects.equals(options, testBlock.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, behaviour, testClass, description, testBlocks, hooks, tests, options);
    }

    @Override
    public String toString() {
        return "TestBlock{"
            + "type=" + type
            + ", behaviour=" + behaviour
            + ", testClass=" + testClass
            + ", description='" + description + '\''
            + ", testBlocks=" + testBlocks
            + ", hooks=" + hooks
            + ", tests=" + tests
            + ", options=" + options
            + '}';
    }

    /**
     * Get all the registered hooks of the given type, in the order they were defined.
     *
     * @param type The type of hook to filter on.
     * @return An immutable list of hooks.
     */
    public List<Hook> hooksOfType(HookType type) {
        return hooks.stream()
                .filter(h -> h.type == type)
                .collect(Collectors.toList());
    }
}
