/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('InstanceService', function ($http) {
        var insts = null;
        var inst = null;

        return {
            /**
             * TODO: WILL THIS METHOD EXIST OR IT WILL BE TAKEN FROM APPLICATION LIST?
             * Method to get the customization list without pagination
             * @returns {*}
             */

            findAll: function (page, size, sortBy, sortOrder, filter) {
                //TODO: This endpoint should be "/api/customization" to be RESTFul.
                var endpoint = '/api/customization/list' +
                    '?page='+ page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter;
                return $http.get(endpoint)
                    .success(function(instances) {
                        insts = instances;
                    });
            },
            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the instances list without pagination
             * @returns {*}
             */
            findAllUnpaginated: function () {
                //return $http.get('/api/customization/listunpaginated')
                return $http.get('mocks/instances.js')
                    .success(function(instances){
                        insts = instances;
                    });
            },
            findById: function (id) {
                return $http.get('/api/customization/' + id)
                    .success(function (instance) {
                        inst = instance;
                    });
            }
        };
    }
);