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

            findAll: function (callback/*page, size, sortBy, sortOrder, filter, owner*/) {
                //TODO: This endpoint should be "/api/customization" to be RESTFul.
                /*
                var endpoint = baseURI +
                    '?page='+ page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter +
                    '&owner=' + owner;
                */
                return $http.get(baseURI )
                    .success(function(instances) {
                        insts = instances;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.findAll error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            findById: function (id) {
                return $http.get(baseURI + SERVICE.SEPARATOR + id)
                    .success(function (instance) {
                        inst = instance;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.findById error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            create: function(instance, callback) {
                return $http.post(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.create error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            start: function(instance, callback) {
                instance.status = "start";
                return $http.put(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.start error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            stop: function(instance, callback) {
                instance.status = "stop";
                return $http.put(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.stop error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            deploy: function(instance, callback) {
                instance.status = "deploy";
                $http.put(baseURI, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.deploy error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            delete: function(instance, callback) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + instance.id)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.delete error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            update: function(instance, callback) {
                return $http.put(baseURI + SERVICE.SEPARATOR, angular.toJson(instance))
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("InstanceService.update error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        };
    }
);