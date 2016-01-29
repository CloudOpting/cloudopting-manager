/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('UserService', function (SERVICE, $http, $log) {
        var usrs = null;
        var usr = null;
        var baseURI = 'api/users';

        return {
            /**
             * Method to get the users applying filters, and with pagination
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
                    .success(function (users) {
                        usrs = users;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.findAll error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                    });
            },
            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the users list without pagination
             * @returns {*}
             */
            findAllUnpaginated: function (callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + 'unpaginated')
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.findAllUnpaginated error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            findById: function (id) {
                return $http.get(baseURI + SERVICE.SEPARATOR + id)
                    .success(function (user) {
                        usr = user;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.findById error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                    });
            },
            create: function (user, callback) {
                return $http.post(baseURI, user)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.create error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            update: function (user, callback) {
                return $http.put(baseURI, user)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.update error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            delete: function (idUser, callback) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + idUser)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.delete error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        }
    }
);