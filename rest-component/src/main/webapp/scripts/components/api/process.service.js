/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('ProcessService', function ($http) {

        var baseURI = 'api/process';
        var baseURITest = 'api/processTest';
        var header = {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'};

        return {
            //FIXME: This function should be deleted together with the Button Screen.
            apiProcessOne: function () {
                return $http.post( baseURI + '?customizationId=4308&isDemo=false&isTesting=true', {}, { headers: header } )
                    .success(function (data, status, headers, config) {
                       //Do we need to deal with the returned data here.
                        var a = data;
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("ProcessService.apiProcessOne error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            test: function(instance, callback) {
                return $http.post( baseURITest + '?customizationId='+instance.id+'&isDemo=false&isTesting=true', {}, { headers: header , responseType: "blob"} )
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("ProcessService.test error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            demo: function(instance, callback) {
                return $http.post( baseURI + '?customizationId='+instance.id+'&isDemo=true&isTesting=false', {}, { headers: header } )
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("ProcessService.demo error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            deploy: function(instance, callback) {
                return $http.post( baseURI + '?customizationId='+instance.id+'&isDemo=false&isTesting=false', {}, { headers: header } )
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("ProcessService.deploy error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        };
    }
);