/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

public class ReportSectionSource extends Source {

    private ReportSource reportSource;
    private ReportSectionSource parentReportSection;
    private String name;

    public ReportSource getReportSource() {
        return reportSource;
    }

    public void setReportSource(ReportSource reportSource) {
        this.reportSource = reportSource;
    }

    public ReportSectionSource getParentReportSection() {
        return parentReportSection;
    }

    public void setParentReportSection(ReportSectionSource parentReportSection) {
        this.parentReportSection = parentReportSection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String buildQualifiedName() {
        if (parentReportSection != null) {
            return parentReportSection.buildQualifiedName() + "." + name;
        } else {
            return reportSource.buildQualifiedName() + "." + name;
        }
    }

}
