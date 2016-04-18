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
             * Method to retrieve all the objects for the instance with identification 'instanceId'.
             * @param instanceId Identification of the instance.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findAllObjectsByInstance: function(instanceId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    instanceId;
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findAllObjectsByInstance error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to retrieve all the zabbix hosts for the instance with identification 'instanceId'.
             * @param instanceId Identification of the instance.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findAllZabbixHosts: function(instanceId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    'hosts' +
                    SERVICE.SEPARATOR +
                    instanceId;
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findAllZabbixHosts error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to retrieve all the zabbix items for the instance with identification 'instanceId'
             * in the host with identification 'hostId'.
             * @param instanceId Identification of the instance.
             * @param hostId Identification of the host.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findAllZabbixItems: function(instanceId, hostId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    'items' +
                    SERVICE.SEPARATOR +
                    instanceId +
                    SERVICE.SEPARATOR +
                    hostId;
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findAllZabbixItems error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to find the zabbix hisotry for the instance with identification 'instanceId'
             * in the host with identification 'hostId' and for the item with identification 'itemId'.
             * @param instanceId Identification of the instance.
             * @param hostId Identification of the host.
             * @param itemId Identification of the item.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findZabbixHistory: function(instanceId, hostId, itemId, tsStart, tsEnd, callback) {
            	console.log(tsStart);
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    'history' +
                    SERVICE.SEPARATOR +
                    instanceId +
                    SERVICE.SEPARATOR +
                    hostId +
                    SERVICE.SEPARATOR +
                    itemId;
                return $http.get(endpoint,{params:{startts:tsStart, endts: tsEnd}})
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findZabbixHistory error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get an object for the instance with identification 'instanceId'.
             * @param instanceId Identification of the instance.
             * @param objectId Identification of the object.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findObject: function(instanceId, objectId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    instanceId +
                    SERVICE.SEPARATOR +
                    objectId;
                return $http.get(endpoint)
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findObject error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * //FIXME: The parameters have to be defined.
             * Method to retrieve monitoring data for the instance with identification 'instanceId'.
             * @param container
             * @param condition
             * @param fields
             * @param type
             * @param pagination
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            getMonitoringData: function(container, condition, fields, type, pagination, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    'elastic' +
                    '?container=' + container +
                    '&condition=' + condition +
                    '&fields=' + fields +
                    '&type=' + type +
                    '&pagination=' + pagination;
                return $http.post(endpoint, {}, { headers: header } )
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.getMonitoringData error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to retrieve the data for the instance with identification 'instanceId'.
             * @param instanceId Identification of the instance.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findOneDataById: function(instanceId, startDate, endDate, callback) {
            	console.log(startDate);
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    'elastic' +
                    SERVICE.SEPARATOR +
                    instanceId;
                return $http.get(endpoint,{params:{startdate:startDate, enddate: endDate}})
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findOneDataById error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to retrieve elastic data for the instance with identification 'instanceId'.
             * @param instanceId Identification of the instance.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findByCustomizationId: function(instanceId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    'elastic/info/list' +
                    SERVICE.SEPARATOR +
                    instanceId;
                return $http.get(endpoint)
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.findByCustomizationId error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to retrieve the status for the instance with identification 'instanceId'.
             * @param instanceId Identification of the instance.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            getStatusById: function(instanceId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    'status' +
                    SERVICE.SEPARATOR +
                    instanceId;
                return $http.get(endpoint)
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("MonitoringService.getStatusById error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        };
    }
);