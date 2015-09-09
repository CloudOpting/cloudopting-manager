/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('ButtonService', function ($http) {
        //{ "customizationId" : "1", "cloudId" : "1" }
        return {
            apiProcessOne: function () {
                return $http.post('api/process?customizationId=1', { },
                    {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
                    })
                    .success(function (data) {
                       //Do we need to deal with the returned data here.
                        var a = data;
                    })
                    .error(function(data, status, headers, config) {
                        var a = data;
                    });
            },
        };
    }
);