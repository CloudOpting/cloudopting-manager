/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('OrganizationService', function (SERVICE, $http, $log) {

        var baseURI = 'api/organization';
        var cloudAccountsURI = "cloudaccounts";

        return {

            /**
             * Method to get the organizations applying filters, and with pagination.
             * @param page Number of the page to be returned.
             * @param size Size of the page.
             * @param sortBy Parameter of the organization that will be used to sort the list.
             * @param sortOrder It can be ascendant 'asc' or descendant 'desc'.
             * @param filter Word that will be used in any field of organization to filter.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findAll: function (page, size, sortBy, sortOrder, filter, callback) {
                var endpoint = baseURI +
                    '?page=' + page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter;
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.findAll error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get an organization with identification 'organizationId'.
             * @param organizationId Identification of the organization.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findById: function (organizationId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    organizationId;
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.findById error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to create an orgznization.
             * @param organization New organization to be created.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            create: function (organization, callback) {
                return $http.post(baseURI, organization)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.create error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to modify an organization.
             * @param organization Organization with the fields to be modified.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            update: function (organization, callback) {
                return $http.put(baseURI, organization)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.update error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to delete an organization with identification 'organizationId'.
             * @param organizationId Identification of the organization.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            delete: function (organizationId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    organizationId;
                return $http.delete(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.delete error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get a the types for the organizations.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            getTypes: function (callback) {
                var endpoint = baseURI + 'Type';
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.getTypes error. " +
                            "Data:" + data + ", status" + status + ", headers" + headers + ", config" + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get a the status for the organizations.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            getStatus: function (callback) {
                var endpoint = baseURI + 'Status';
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.getStatus error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get a cloud accounts for the organization with identification 'organizationId'.
             * @param organizationId Identification of the organization.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            getCloudAccount: function (organizationId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    organizationId +
                    SERVICE.SEPARATOR +
                    cloudAccountsURI;
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.getCloudAccount error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to create a cloud account.
             * @param organizationId Identification of the organization.
             * @param cloudAccount New cloud account to be created.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            createCloudAccount: function (organizationId, cloudAccount, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    organizationId +
                    SERVICE.SEPARATOR +
                    cloudAccountsURI;
                return $http.post(endpoint, cloudAccount)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.createCloudAccount error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to update a cloud account for the organization with identification 'organizationId'.
             * @param organizationId Identification of the organization.
             * @param cloudAccount Cloud account with the fields to be modified.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            updateCloudAccount: function (organizationId, cloudAccount, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    organizationId +
                    SERVICE.SEPARATOR +
                    cloudAccountsURI;
                return $http.put(endpoint, cloudAccount)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.updateCloudAccount error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to delete a cloud account for the organization with identification 'organizationId'.
             * @param organizationId Identification of the organization.
             * @param cloudAccountId Identification of the cloud account.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            deleteCloudAccount: function (organizationId, cloudAccountId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    organizationId +
                    SERVICE.SEPARATOR +
                    cloudAccountsURI +
                    SERVICE.SEPARATOR +
                    cloudAccountId;
                return $http.delete(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.deleteCloudAccount error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        }
    }
);