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

/**
 * A condition that checks if a tag is contains in a collection of tags.
 */
class ContainsCondition implements Condition {

    private String tag;

    /**
     * Constructor.
     *
     * @param tag A group/tag we want to search for.
     */
    ContainsCondition(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean shouldRun(Collection<String> tags) {
        return tags.contains(tag);
    }
}
