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
                var endpoint = '/api/customization' +
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
            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the instances list without pagination
             * @returns {*}
             */
            findAllUnpaginatedSubscriber: function () {
                //return $http.get('/api/customization/listunpaginated')
                return $http.get('mocks/instancesSubscriber.js')
                    .success(function(instances){
                        insts = instances;
                    });
            },
            findById: function (id) {
                return $http.get('/api/customization/' + id)
                    .success(function (instance) {
                        inst = instance;
                    });
            },
            create: function(instance) {
                return $http.post('/api/customization', angular.toJson(instance))
                    .success(function (data) {
                        //TODO: Do something if all went ok.
                    });
            },
            start: function(instance) {
                instance.status = "start";
                return $http.put('/api/customization', angular.toJson(instance))
                    .success(function (data) {
                        //TODO: Do something if all went ok.
                    });
            },
            stop: function(instance) {
                instance.status = "stop";
                return $http.put('/api/customization', angular.toJson(instance))
                    .success(function (data) {
                        //TODO: Do something if all went ok.
                    });
            },
            deploy: function(instance) {
                instance.status = "deploy";
                return $http.put('/api/customization', angular.toJson(instance))
                    .success(function (data) {
                        //TODO: Do something if all went ok.
                    });
            },
            delete: function(instance) {
                return $http.delete('/api/customization/' + instance.id);
            },
        };
    }
);