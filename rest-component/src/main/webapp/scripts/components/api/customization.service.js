/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('CustomizationService', function (SERVICE, $http, $log) {

        var baseURI = 'api/application';

        return {

            /**
             * Method to get the custom form for the application with id 'applicationId'.
             * @param applicationId Identification of the application.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            getCustomizationForm: function(applicationId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId +
                    SERVICE.SEPARATOR +
                    'getCustomizationForm';
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("CustomizationService.getCustomizationForm error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to send and save the customization form retrieved with @getCustomizaitonForm method.
             * @param applicationId Identification of the application.
             * @param jsonFormData JSON representing a key/value object with the customized fields from @getCustomizaitonForm method.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            sendCustomizationForm: function(applicationId, jsonFormData, callback) {
                var config = {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    transformRequest: function(obj) {
                        var str = [];
                        for(var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    }
                };
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId +
                    SERVICE.SEPARATOR +
                    'sendCustomizationForm';
                console.debug(endpoint);
                return $http.post( endpoint, { formData: angular.toJson(jsonFormData) }, config )
                    .success(function(data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function(data, status, headers, config) {
                        $log.error("CustomizationService.sendCustomizationForm error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }
        };
    }
);