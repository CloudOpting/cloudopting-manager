/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('OrganizationService', function (SERVICE, $http, $log) {
        var orgs = null;
        var org = null;
        var status = null;
        var types = null;
        var baseURI = 'api/organization';
        var cloudAccountsURI = "cloudaccounts";

        return {
            /**
             * Method to get the organizations applying filters, and with pagination
             * @returns {*}
             */
            findAll: function (page, size, sortBy, sortOrder, filter) {
                var endpoint = baseURI +
                    '?page=' + page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter;
                return $http.get(endpoint)
                    .success(function (organizations) {
                        orgs = organizations;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.findAll error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                    });
            },
            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the organizations list without pagination
             * @returns {*}
             */
            findAllUnpaginated: function (callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + 'unpaginated')
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.findAllUnpaginated error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            findById: function (id) {
                return $http.get(baseURI + SERVICE.SEPARATOR + id)
                    .success(function (organization) {
                        org = organization;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.findById error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                    });
            },
            create: function (organization, callback) {
                return $http.post(baseURI, organization)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.create error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            update: function (organization, callback) {
                return $http.put(baseURI, organization)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.update error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            delete: function (idOrganization, callback) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + idOrganization)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.delete error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            getTypes: function () {
                return $http.get(baseURI + 'Type')
                    .success(function (typess) {
                        types = typess;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.getTypes error. Data:" + data + ", status" + status + ", headers" + headers + ", config" + config);
                    });
            },
            getStatus: function () {
                return $http.get(baseURI + 'Status')
                    .success(function (statuss) {
                        status = statuss;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.getStatus error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                    });
            },
            getCloudAccount: function (idOrganization, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + idOrganization + SERVICE.SEPARATOR + cloudAccountsURI)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.getCloudAccount error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            createCloudAccount: function (idOrganization, cloudAccount, callback) {
                return $http.post(baseURI+ SERVICE.SEPARATOR + idOrganization + SERVICE.SEPARATOR + cloudAccountsURI, cloudAccount)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.createCloudAccount error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            updateCloudAccount: function (idOrganization, cloudAccount, callback) {
                return $http.put(baseURI+ SERVICE.SEPARATOR + idOrganization + SERVICE.SEPARATOR + cloudAccountsURI, cloudAccount)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.updateCloudAccount error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            deleteCloudAccount: function (idOrganization, idCloudAccount, callback) {
                return $http.delete(baseURI+ SERVICE.SEPARATOR + idOrganization + SERVICE.SEPARATOR + cloudAccountsURI + SERVICE.SEPARATOR + idCloudAccount)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("OrganizationService.deleteCloudAccount error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        }
    }
);