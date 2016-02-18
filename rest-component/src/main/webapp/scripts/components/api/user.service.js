/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('UserService', function (SERVICE, $http, $log) {

        var baseURI = 'api/users';

        return {

            /**
             * Method to get the users applying filters, and with pagination.
             * @param page Number of the page to be returned.
             * @param size Size of the page.
             * @param sortBy Parameter of the user that will be used to sort the list.
             * @param sortOrder It can be ascendant 'asc' or descendant 'desc'.
             * @param filter Word that will be used in any field of user to filter.
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
                        $log.error("UserService.findAll error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get a user with identification 'userId'.
             * @param userId Identification of the user.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findById: function (userId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    userId;
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.findById error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             *Method to create a user.
             * @param user New user to be created.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            create: function (user, callback) {
                return $http.post(baseURI, user)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.create error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to update a user.
             * @param user User with the fields to be modified.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            update: function (user, callback) {
                return $http.put(baseURI, user)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.update error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to delete a user with identification 'userId'.
             * @param userId Identification of the user.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            delete: function (userId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    userId;
                return $http.delete(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("UserService.delete error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        }
    }
);