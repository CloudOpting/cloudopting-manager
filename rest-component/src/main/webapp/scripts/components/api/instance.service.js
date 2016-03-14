/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('InstanceService', function (SERVICE, $http, $log) {

        var baseURI = 'api/customization';

        return {

            /**
             * TODO: WILL THIS METHOD EXIST OR IT WILL BE TAKEN FROM APPLICATION LIST?
             * Method to get the customization list without pagination
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findAll: function (callback/*page, size, sortBy, sortOrder, filter, owner*/) {
                /*
                var endpoint = baseURI +
                    '?page='+ page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter +
                    '&owner=' + owner;
                */
                return $http.get(baseURI)
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.findAll error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get an instance by the id.
             * @param instanceId Identification of the instance.
             * @returns {*}
             */
            findById: function (instanceId) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    instanceId;
                return $http.get(endpoint)
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.findById error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to create an instance.
             * @param instance New instance to be created.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            create: function(instance, callback) {
                return $http.post(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.create error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },


            /**
             * Method to update an instance.
             * @param instance Instance with the fields to be modified.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            update: function(instance, callback) {
                return $http.put(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.update error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to delete an instance with identification 'instanceId'.
             * @param instanceId Identification of the instance.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            delete: function(instanceId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    instanceId;
                return $http.delete(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.delete error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to start the given instance.
             * @param instance Instance to be started.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            start: function(instance, callback) {
                instance.status = "start";
                return $http.put(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.start error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to stop the given instance.
             * @param instance Instance to be stoped.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            stop: function(instance, callback) {
                instance.status = "stop";
                return $http.put(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.stop error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to deploy the given instance.
             * @param instance Instance to be deployed.
             * @param callback Function that will take care of the returned objects.
             */
            deploy: function(instance, callback) {
                instance.status = "deploy";
                $http.put(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.deploy error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }

        };
    }
);