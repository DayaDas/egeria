/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The GovernanceProgramAuditCode is used to define the message content for the OMRS Audit Log.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum GovernanceProgramAuditCode
{
    SERVICE_INITIALIZING("OMAS-GOVERNANCE-PROGRAM-0001",
              OMRSAuditLogRecordSeverity.INFO,
              "The Governance Program Open Metadata Access Service (OMAS) is initializing a new server instance",
              "The local server has started up a new instance of the Governance Program OMAS.",
              "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAS-GOVERNANCE-PROGRAM-0002",
              OMRSAuditLogRecordSeverity.INFO,
              "The Governance Program Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server {0}",
              "The Governance Program OMAS is registering to receive events from the connected open metadata repositories.",
              "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OMAS-GOVERNANCE-PROGRAM-0003",
              OMRSAuditLogRecordSeverity.INFO,
              "The Governance Program Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
              "The Governance Program OMAS has completed initialization of a new instance.",
              "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-GOVERNANCE-PROGRAM-0004",
              OMRSAuditLogRecordSeverity.INFO,
              "The Governance Program Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
              "The local server has requested shut down of an Governance Program OMAS instance.",
              "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INSTANCE_FAILURE("OMAS-ASSET-CONSUMER-0005",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Asset Consumer Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.")

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(GovernanceProgramAuditCode.class);


    /**
     * The constructor for GovernanceProgramAuditCode expects to be passed one of the enumeration rows defined in
     * GovernanceProgramAuditCode above.   For example:
     *
     *     GovernanceProgramAuditCode   auditCode = GovernanceProgramAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    GovernanceProgramAuditCode(String                     messageId,
                               OMRSAuditLogRecordSeverity severity,
                               String                     message,
                               String                     systemAction,
                               String                     userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== GovernanceProgram Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> GovernanceProgram Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }
}
