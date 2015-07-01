/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('ApplicationService', function ($http, Upload) {
        var apps = null;
        var app = null;
        //TODO: Is the BASE URI needed for the Upload component?
        var restBaseURI = "";

        return {
            /**
             * Method to get the applications applying filters, and with pagination
             * @returns {*}
             */
            findAll: function (page, size, sortBy, sortOrder, filter) {
                //TODO: This endpoint should be "/api/application" to be RESTFul.
                var endpoint = '/api/application/list' +
                    '?page='+ page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter;
                return $http.get(endpoint)
                    .success(function(applications) {
                        apps = applications;
                    });
            },
            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the application list without pagination
             * @returns {*}
             */
            findAllUnpaginated: function () {
                //return $http.get('/api/application/listunpaginated')
                return $http.get('mocks/applications.js')
                    .success(function(applications){
                        apps = applications;
                    });
            },
            findById: function (id) {
                return $http.get('/api/application/' + id)
                    .success(function (application) {
                        app = application;
                    });
            },
            create: function(name, description, files, callback) {
                if (files && files.length) {
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        Upload.upload({
                            method: 'POST',
                            headers: { 'Authorization' : 'Basic YWRtaW46YWRtaW4=' },
                            url: restBaseURI + '/application',
                            fields: {'name': name, 'description': description},
                            file: file
                        }).progress(function (evt) {
                            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                            $log.debug('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                        }).success(function (data, status, headers, config) {
                            callback(data);
                            $log.debug('Application ID:  ' + data);
                        });
                    }
                }
            },
            addContentLibrary: function (idApplication, libraryList, libraryName, callback) {
                if (libraryList && libraryList.length) {
                    for (var i = 0; i < libraryList.length; i++) {
                        var file = libraryList[i];
                        Upload.upload({
                            method: 'POST',
                            headers: { 'Authorization' : 'Basic YWRtaW46YWRtaW4=' },
                            url: restBaseURI + '/application/'+idApplication+'/contentlibrary',
                            fields: { 'name' : libraryName},
                            file: file
                        }).progress(function (evt) {
                            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                            $log.debug('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                        }).success(function (data, status, headers, config) {
                            callback(data);
                            $log.debug('file ' + config.file.name + 'uploaded. Response: ' + data);
                        });
                    }
                }
            },
            deleteContentLibrary: function (idApplication, fileId) {
                return $http.delete('api/application/'+idApplication+'/contentlibrary/'+fileId);
            },
            addToscaFile: function (toscaFile, idApplication) {
                Upload.upload({
                    method: 'POST',
                    headers: { 'Authorization' : 'Basic YWRtaW46YWRtaW4=' },
                    url: restBaseURI + '/application/'+idApplication+'/tosca',
                    file: toscaFile
                }).progress(function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    $log.debug('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function (data, status, headers, config) {
                    $log.debug('file ' + config.file.name + 'uploaded. Response: ' + data);
                });
            },
            inputParameters: function (id) {
                //return $http.get('/api/application/'+id+'inputparameters')
                return $http.get('mocks/inputParameters.js')
                    .success(function(applications){
                        apps = applications;
                    });
            },
            /**
             * Method to get the application parameters to be customized.
             *
             * @returns {*}
             */
            requestPublication: function (idApplication) {
                return $http({
                    method: 'POST',
                    headers: { 'Authorization' : 'Basic YWRtaW46YWRtaW4=' },
                    url: restBaseURI + '/application/'+idApplication+'/publish'
                });
            }
        };
    }
);