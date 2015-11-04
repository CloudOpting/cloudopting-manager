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
                        callback(data);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("Something went wrong" + data);
                    });
            },
            findById: function (id) {
                return $http.get(baseURI + SERVICE.SEPARATOR + id)
                    .success(function (user) {
                        usr = user;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("Something went wrong" + data);
                    });
            },
            create: function (user, callback) {
                return $http.post(baseURI, user)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("Something went wrong" + data);
                    });
            },
            update: function (user, callback) {
                return $http.put(baseURI, user)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("Something went wrong" + data);
                    });
            },
            delete: function (idUser, callback) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + idUser)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("Something went wrong" + data);
                    });
            }
        }
    }
);