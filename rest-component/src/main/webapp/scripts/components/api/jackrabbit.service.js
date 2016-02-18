/**
 * Created by a591584 on 16/02/2016.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('JackrabbitService', function (SERVICE, $http, $log) {

        var baseURI = 'api/jr/img';

        return {

            /**
             * Method to get an image from jackrabbit repository with its full path url 'jackRabbitFullUrl'.
             * @param jackrabbitFullUrl Full jackrabbit url of the image.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findImage: function (jackrabbitFullUrl, callback) {
                var config =  {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    transformRequest: function(obj) {
                        var str = [];
                        for(var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    }
                };

                return $http.get(baseURI, {params: {jcrPath: jackrabbitFullUrl}}, config)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("JackrabbitService.findImage error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        }
    }
);