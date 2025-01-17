/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.ffdc;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

import java.util.Objects;

public class NoSchemaAttributeException extends OCFCheckedExceptionBase {
    private final String qualifiedName;


    public NoSchemaAttributeException(int httpCode, String className, String actionDescription, String errorMessage,
                                      String systemAction, String userAction, String qualifiedName) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.qualifiedName = qualifiedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NoSchemaAttributeException that = (NoSchemaAttributeException) o;
        return Objects.equals(qualifiedName, that.qualifiedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), qualifiedName);
    }

    @Override
    public String toString() {
        return "NoSchemaAttributeException{" +
                "qualifiedName='" + qualifiedName + '\'' +
                '}';
    }

    public String getQualifiedName() {
        return qualifiedName;
    }
}
