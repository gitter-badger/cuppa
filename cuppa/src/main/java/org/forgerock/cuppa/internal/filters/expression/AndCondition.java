/*
 * Copyright 2018 ForgeRock AS.
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

package org.forgerock.cuppa.internal.filters.expression;

import java.util.Collection;
import java.util.Collections;

/**
 * A condition that composes other conditions with a logical AND.
 */
class AndCondition extends ConditionWrapper {

    public static final AndCondition EMPTY = new AndCondition(Collections.emptyList());
    private final Collection<Condition> conditions;

    /**
     * Constructor.
     *
     * @param conditions a list of condition to compose.
     */
    AndCondition(Collection<Condition> conditions) {
        this.conditions = Collections.unmodifiableCollection(conditions);
    }

    @Override
    public boolean shouldRun(Collection<String> tags) {
        return conditions.stream().allMatch(c -> c.shouldRun(tags));
    }

    @Override
    public ConditionWrapper setConditions(Collection<Condition> conditions) {
        return new AndCondition(conditions);
    }
}
