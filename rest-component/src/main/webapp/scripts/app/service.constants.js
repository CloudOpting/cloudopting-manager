"use strict";
// DO NOT EDIT THIS FILE UNLESS YOU KNOW WHAT ARE YOU MODIFYING.
angular.module('cloudoptingApp')
    .constant("SERVICE", {
        "SEPARATOR" : "/",
        "ROLE" : {
            "PUBLISHER"  : "ROLE_PUBLISHER",
            "ADMIN"  : "ROLE_ADMIN",
            "OPERATOR"  : "ROLE_OPERATOR",
            "SUBSCRIBER"  : "ROLE_SUBSCRIBER"
        },
        "STATUS" : {
            "UNFINISHED" : "UNFINISHED",
            "UPLOADED" : "UPLOADED",
            "DRAFT" : "DRAFT",
            "READY_TO_PUBLISH" : "READY_TO_PUBLISH",
            "PUBLISHED" : "PUBLISHED"
        },
        "STORAGE" : {
            "ACTIVITI" : {
                "PROCESS_ID" : "activitiProcessId"
            },
            "DETAIL" : {
                "APPLICATION" : "detailApplication"
            },
            "CHOOSE_ACCOUNT" : {
                "INSTANCE" : "chooseAccountInstance"
            },
            "CLOUD_ACCOUNT" : {
                "CLOUD_ACCOUNT" : "cloudAccountCloudAccount",
                "ORGANIZATION" : "cloudAccountOrganization"
            },
            "MONITORING" : {
                "INSTANCE" : "monitoringInstance"
            },
            "FORM_GENERATION" : {
                "APPLICATION" : "formGenerationApplication"
            },
            "INSTANCES" : {
                "APPLICATION" : "instancesApplication"
            },
            "PUBLISH" : {
                "APPLICATION" : "publishApplication",
                "IS_EDITION" : "isEdition",
                "ACTIVITI" : "publishActiviti"
            },
            "PROFILE" : {
                "CLOUD_ACCOUNT" : "profileCloudAccount"
            },
            "USER_MANAGER" : {
                "USER" : "userManagerUser"
            },
            "ORG_MANAGER" : {
                "ORGANIZATION" : "orgManagerOrganization"
            },
            "TAYLOR" : {
                "APPLICATION" : "taylorInstancesApplication"
            },
            "ADD_DEPLOY" : {
                "APPLICATION" : "taylorInstancesApplication"
            }
        },
        "FILE_TYPE" : {
            "PROMO_IMAGE" : "promoimage",
            "CONTENT_LIBRARY" : "contentlibrary",
            "TOSCA_ARCHIVE" : "toscaarchive"
        }
    }
);