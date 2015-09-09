/**
 * Created by a591584 on 29/07/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('MonitoringService', function (SERVICE, $http, $log) {

        var baseURI = 'api/monitoring';

        return {
            /**
             * The the list of monitored objects
             * @param instanceId
             * @param callback
             */
            findAllObjectsByInstance: function(instanceId, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + instanceId)
                    .success(function (data, status, headers, config) {
                        callback(data);
                    }
                )
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
                        callback(data);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error(data);
                    })
                    ;
            }
        };
    }
);