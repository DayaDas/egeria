/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.services;


import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.errorcode.SecurityOfficerErrorCode;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Builder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_LABELS;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_OFFICER;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_PROPERTIES;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_TAGS;

/**
 * SecurityOfficerInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SecurityOfficerAdmin class.
 */
class SecurityOfficerInstanceHandler {

    private static SecurityOfficerServicesInstanceMap instanceMap = new SecurityOfficerServicesInstanceMap();
    private Builder builder = new Builder();

    /**
     * Default constructor registers the access service
     */
    SecurityOfficerInstanceHandler() {
        SecurityOfficerRegistration.registerAccessService();
    }

    /**
     * Return the repository connector for this server.
     *
     * @return OMRSRepositoryConnector object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    OMRSRepositoryConnector getRepositoryConnector(String serverName) throws PropertyServerException {
        SecurityOfficerServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getRepositoryConnector();
        } else {
            final String methodName = "getRepositoryConnector";

            SecurityOfficerErrorCode errorCode = SecurityOfficerErrorCode.SERVICE_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    SecurityClassification getSecurityTagBySchemaElementId(String serverName, String userId, String schemaElementId) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {
        OMRSMetadataCollection metadataCollection = getRepositoryConnector(serverName).getMetadataCollection();

        EntityDetail entityDetail = metadataCollection.getEntityDetail(userId, schemaElementId);
        List<Classification> classifications = entityDetail.getClassifications();
        Optional<Classification> securityTag = classifications.stream().filter(classification -> classification.getName().equals(SECURITY_TAGS)).findAny();

        OMRSRepositoryHelper repositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();
        return securityTag.map(classification -> builder.buildSecurityTag(classification, repositoryHelper)).orElse(null);

    }

    SchemaElementEntity updateSecurityTagBySchemaElementId(String serverName, String userId, String schemaElementId, SecurityClassification securityClassification) throws PropertyServerException, RepositoryErrorException, ClassificationErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, EntityProxyOnlyException {
        OMRSMetadataCollection metadataCollection = getRepositoryConnector(serverName).getMetadataCollection();

        InstanceProperties instanceProperties = getInstanceProperties(serverName, securityClassification);
        EntityDetail schemaElement = metadataCollection.getEntityDetail(userId, schemaElementId);

        EntityDetail entityDetail;
        if (schemaElement.getClassifications() != null && !schemaElement.getClassifications().isEmpty()) {
            entityDetail = metadataCollection.updateEntityClassification(userId, schemaElementId, SECURITY_TAGS, instanceProperties);

        } else {
            entityDetail = metadataCollection.classifyEntity(userId, schemaElementId, SECURITY_TAGS, instanceProperties);
        }

        OMRSRepositoryHelper omrsRepositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();
        return builder.buildSchemaElement(entityDetail, omrsRepositoryHelper);
    }

    private InstanceProperties getInstanceProperties(String serverName, SecurityClassification securityTagLevel) throws PropertyServerException {
        InstanceProperties instanceProperties = new InstanceProperties();
        String methodName = "getInstanceProperties";
        OMRSRepositoryHelper repositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();

        repositoryHelper.addMapPropertyToInstance(SECURITY_OFFICER, instanceProperties, SECURITY_PROPERTIES, securityTagLevel.getSecurityProperties(), methodName);
        repositoryHelper.addStringArrayPropertyToInstance(SECURITY_OFFICER, instanceProperties, SECURITY_LABELS, securityTagLevel.getSecurityLabels(), methodName);

        return instanceProperties;
    }
}
