/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('InstanceService', function (SERVICE, $http) {
        var baseURI = 'api/customization';
        var insts = null;
        var inst = null;

        return {
            /**
             * TODO: WILL THIS METHOD EXIST OR IT WILL BE TAKEN FROM APPLICATION LIST?
             * Method to get the customization list without pagination
             * @returns {*}
             */

            findAll: function (page, size, sortBy, sortOrder, filter, owner) {
                //TODO: This endpoint should be "/api/customization" to be RESTFul.
                var endpoint = baseURI +
                    '?page='+ page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter +
                    '&owner=' + owner;
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
                //return $http.get(baseURI+'/listunpaginated')
                return $http.get('mocks/instances.js')
                    .success(function(instances){
                        insts = instances;
                    });
            },
            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the instances list without pagination
             * This method should be replaced by findAll with the owner filter.
             * @returns {*}
             */
            findAllUnpaginatedSubscriber: function () {
                //return $http.get(baseURI+'/listunpaginated')
                return $http.get('mocks/instancesSubscriber.js')
                    .success(function(instances){
                        insts = instances;
                    });
            },
            findById: function (id) {
                return $http.get(baseURI + SERVICE.SEPARATOR + id)
                    .success(function (instance) {
                        inst = instance;
                    });
            },
            create: function(instance) {
                return $http.post(baseURI, angular.toJson(instance))
                    .success(function (data) {
                        //TODO: Do something if all went ok.
                    });
            },
            start: function(instance) {
                instance.status = "start";
                return $http.put(baseURI, angular.toJson(instance))
                    .success(function (data) {
                        //TODO: Do something if all went ok.
                    });
            },
            stop: function(instance) {
                instance.status = "stop";
                return $http.put(baseURI, angular.toJson(instance))
                    .success(function (data) {
                        //TODO: Do something if all went ok.
                    });
            },
            deploy: function(instance, callback) {
                //TODO: Ask for the cloud account needed.
                instance.status = "deploy";
                $http.put(baseURI, angular.toJson(instance))
                    .success(function (data) {
                        if(callback) { callback(data); }
                    });
            },
            delete: function(instance) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + instance.id);
            }
        };
    }
);