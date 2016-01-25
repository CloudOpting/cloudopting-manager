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
                        callback(data);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("Something went wrong" + data);
                    });
            },
            findById: function (id) {
                return $http.get(baseURI + SERVICE.SEPARATOR + id)
                    .success(function (organization) {
                        org = organization;
                    });
            },
            create: function (organization, callback) {
                return $http.post(baseURI, organization)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    });
            },
            update: function (organization, callback) {
                return $http.put(baseURI, organization)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    });
            },
            delete: function (idOrganization) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + idOrganization)
                    .success(function (data, status, headers, config) {
                        //callback(data);
                    });
            },
            getTypes: function () {
                return $http.get(baseURI + 'Type')
                    .success(function (typess) {
                        types = typess;
                    });
            },
            getStatus: function () {
                return $http.get(baseURI + 'Status')
                    .success(function (statuss) {
                        status = statuss;
                    });
            },
            getCloudAccount: function (idOrganization, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + idOrganization + SERVICE.SEPARATOR + cloudAccountsURI)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    });
            },
            createCloudAccount: function (idOrganization, cloudAccount, callback) {
                return $http.post(baseURI+ SERVICE.SEPARATOR + idOrganization + SERVICE.SEPARATOR + cloudAccountsURI, cloudAccount)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    });
            },
            updateCloudAccount: function (idOrganization, cloudAccount, callback) {
                return $http.put(baseURI+ SERVICE.SEPARATOR + idOrganization + SERVICE.SEPARATOR + cloudAccountsURI, cloudAccount)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    });
            },
            deleteCloudAccount: function (idOrganization, idCloudAccount, callback) {
                return $http.delete(baseURI+ SERVICE.SEPARATOR + idOrganization + SERVICE.SEPARATOR + cloudAccountsURI + SERVICE.SEPARATOR + idCloudAccount)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    });
            }
        }
    }
);