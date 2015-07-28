/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('CustomizationService', function (SERVICE, $http, $log) {

        var baseURI = '/api/application';

        return {
            /**
             * BEGINNING of the integration for DYNAMIC FORMS
             */
            /**
             * Get the custom form.
             * @param idApp
             * @param callback
             * @returns {*}
             */
            getCustomizationForm: function(idApp, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + idApp + SERVICE.SEPARATOR + "getCustomizationForm")
                    .success(function (data, status, headers, config) {
                        callback(data);
                        /*        return {
                         type: "object",
                         properties: {
                         name: { type: "string", minLength: 2, title: "Name", description: "Name or alias" },
                         title: {
                         type: "string",
                         enum: ['dr','jr','sir','mrs','mr','NaN','dj']
                         }
                         }
                         };
                         */
                    }
                )
            },

            /**
             * in the post place the json in a request parameter called formData, as a string
             * @param json
             * @param callback
             * @returns {*}
             */
            sendCustomizationForm: function(json, callback) {
                return $http.post(baseURI + SERVICE.SEPARATOR + 'sendCustomizationForm', {formData: json})
                    .success(function(data, status, headers, config) {
                        callback(data);
                    }
                );
            }
        };
    }
);