/**
 * Created by a591584 on 16/02/2016.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('JackrabbitService', function (SERVICE, $http, $log) {
        var baseURI = 'api/jr/img';

        return {
            findImage: function (url, callback) {
                var config =  {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    transformRequest: function(obj) {
                        var str = [];
                        for(var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    }
                };

                return $http.get(baseURI, {params: {jcrPath: url}}, config)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("JackrabbitService.findImage error. Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        }
    }
);
/*
 ,
 transformRequest: function(obj) {
 var str = [];
 for(var p in obj)
 str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
 return str.join("&");
 }
 */