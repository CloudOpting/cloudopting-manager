/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('ProcessService', function ($http) {
        //{ "customizationId" : "1", "cloudId" : "1" }
        var baseURI = 'api/process';
        var header = {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'};

        return {
            //FIXME: This function should be deleted together with the Button Screen.
            apiProcessOne: function () {
                return $http.post(
                    baseURI + '?customizationId=4308&cluodId=1&isTesting=true',
                    {},
                    { headers: header }
                )
                    .success(function (data, status, headers, config) {
                       //Do we need to deal with the returned data here.
                        var a = data;
                    })
                    .error(function(data, status, headers, config) {
                        //TODO: Do something if it fails
                    });
            },
            test: function(instance, cloudAccount, callback) {
                return $http.post(
                    baseURI + '?customizationId='+instance.id+'&cluodId='+cloudAccount.id+'isTesting=true',
                    {},
                    { headers: header }
                )
                    .success(function (data, status, headers, config) {
                        if(callback) { callback(data); }
                    })
                    .error(function(data, status, headers, config) {
                        //TODO: Do something if it fails
                    });
            },
            deploy: function(instance, cloudAccount, callback) {
                return $http.post(
                    baseURI + '?customizationId='+instance.id+'&cluodId='+cloudAccount.id+'isTesting=false',
                    {},
                    { headers: header }
                )
                    .success(function (data, status, headers, config) {
                        if(callback) { callback(data); }
                    })
                    .error(function(data, status, headers, config) {
                        //TODO: Do something if it fails
                    });
            }
        };
    }
);