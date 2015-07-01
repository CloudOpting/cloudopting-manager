/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('ApplicationService', function ($http) {
        var apps = null;
        var app = null;

        return {
            /**
             * Method to get the applications applying filters, and with pagination
             * @returns {*}
             */
            findAll: function (page, size, sortBy, sortOrder, filter) {
                //TODO: This endpoint should be "/api/application" to be RESTFul.
                var endpoint = '/api/application/list' +
                    '?page='+ page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter;
                return $http.get(endpoint)
                    .success(function(applications) {
                        apps = applications;
                    });
            },
            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the application list without pagination
             * @returns {*}
             */
            findAllUnpaginated: function () {
                //return $http.get('/api/application/listunpaginated')
                return $http.get('mocks/applications.js')
                    .success(function(applications){
                        apps = applications;
                    });
            },
            findById: function (id) {
                return $http.get('/api/application/' + id)
                    .success(function (application) {
                        app = application;
                    });
            }
        };
    }
);