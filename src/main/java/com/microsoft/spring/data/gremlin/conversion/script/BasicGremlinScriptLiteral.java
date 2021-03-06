/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion.script;

import com.microsoft.spring.data.gremlin.common.Constants;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

public abstract class BasicGremlinScriptLiteral {

    protected void generateProperties(@NonNull List<String> scriptList, @NonNull final Map<String, Object> properties) {

        for (final Map.Entry<String, Object> entry : properties.entrySet()) {
            final String name = entry.getKey();
            final Object value = entry.getValue();

            if (value instanceof String) {
                scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY_STRING, name, value.toString()));
            } else if (value instanceof Integer) {
                scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY_NUMBER, name, value));
            } else if (value instanceof Boolean) {
                scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY_BOOLEAN, name, value));
            } else {
                final String typeName = value.getClass().getName();
                throw new NotImplementedException(String.format("unsupported type %s from gremlin", typeName));
            }
        }
    }
}
