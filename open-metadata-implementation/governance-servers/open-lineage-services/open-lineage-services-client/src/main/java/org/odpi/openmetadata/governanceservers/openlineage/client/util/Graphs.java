/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client.util;

public enum Graphs {

    main("main"),
    buffer("buffer"),
    history("history"),
    mock("mock");

    private String name;

    Graphs(String name) {
        this.name = name;
    }


}

