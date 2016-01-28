/**
 * Created by a591584 on 29/07/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('MonitoringService', function (SERVICE, $http, $log) {

        var baseURI = 'api/monitoring';
        var header = {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'};

        return {
            /**
             * The the list of monitored objects
             * @param instanceId
             * @param callback
             */
            findAllObjectsByInstance: function(instanceId, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + instanceId)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findAllObjectsByInstance error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Get an object fom a specific instance.
             * @param instanceId
             * @param objectId
             * @param callback
             */
            findObject: function(instanceId, objectId, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + instanceId + SERVICE.SEPARATOR + objectId)
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findObject error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            getMonitoringData: function(container, condition, fields, type, pagination, callback) {
                return $http.post(
                    baseURI + SERVICE.SEPARATOR + "elastic" +'?container='+container+'&condition='+condition+'&fields='+fields+'&type='+type+'&pagination='+pagination,
                    {},
                    { headers: header }
                )
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.getMonitoringData error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            
            findOneDataById: function(instanceId, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + "elastic" + SERVICE.SEPARATOR + instanceId)
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findOneDataById error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            //New endpoint to retriebe the data
            findByCustomizationId: function(instanceId, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + "elastic/info/list" + SERVICE.SEPARATOR + instanceId)
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findByCustomizationId error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        };
    }
);