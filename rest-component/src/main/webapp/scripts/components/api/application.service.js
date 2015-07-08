/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('ApplicationService', function (SERVICE, $http, Upload) {
        var apps = null;
        var app = null;
        //TODO: Should the base uri be a full URI for the Upload component?
        var baseURI = '/api/application';

        return {
            /**
             * Method to get the applications applying filters, and with pagination
             * @returns {*}
             */
            findAll: function (page, size, sortBy, sortOrder, filter) {
                var endpoint = baseURI +
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
                return $http.get(baseURI + SERVICE.SEPARATOR + id)
                    .success(function (application) {
                        app = application;
                    });
            },
            create: function(application, files, callback) {
                return $http.post(baseURI + SERVICE.SEPARATOR, application)
                    .success(function(data) {
                        //TODO: Do something if all went ok.
                    });
            },
            addPromotionalImage: function(idApplication, name, description, files, callback) {
                if (files && files.length) {
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        Upload.upload({
                            method: 'POST',
                            headers: { 'Authorization' : 'Basic YWRtaW46YWRtaW4=' },
                            url: baseURI + SERVICE.SEPARATOR + idApplication + SERVICE.SEPARATOR + 'file',
                            fields: { 'name': "promoImage", 'type' : SERVICE.FILE_TYPE.PROMO_IMAGE },
                            file: file
                        }).progress(function (evt) {
                            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                            $log.debug('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                        }).success(function (data, status, headers, config) {
                            callback(data);
                            $log.debug('Application ID: ' + data);
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
                            url: baseURI + SERVICE.SEPARATOR + idApplication + SERVICE.SEPARATOR + 'file',
                            fields: { 'name' : libraryName, 'type' : SERVICE.FILE_TYPE.CONTENT_LIBRARY },
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
            addToscaArchive: function (toscaFile, idApplication) {
                Upload.upload({
                    method: 'POST',
                    headers: { 'Authorization' : 'Basic YWRtaW46YWRtaW4=' },
                    url: baseURI + SERVICE.SEPARATOR + idApplication + SERVICE.SEPARATOR + 'file',
                    fields: { 'name' : "toscaArchive", 'type' : SERVICE.FILE_TYPE.TOSCA_ARCHIVE },
                    file: toscaFile
                }).progress(function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    $log.debug('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function (data, status, headers, config) {
                    $log.debug('file ' + config.file.name + 'uploaded. Response: ' + data);
                });
            },
            deleteAppFile: function (idApplication, fileId) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + idApplication + SERVICE.SEPARATOR + 'file' + SERVICE.SEPARATOR + fileId);
            },
            /**
             * Method to get the application parameters to be customized.
             *
             * @returns {*}
             */
            requestPublication: function (application) {
                application.status = "Published";
                return $http.put(baseURI + SERVICE.SEPARATOR + application.id, angular.toJson(application))
                    .success(function(data) {
                        //TODO: Do something if all went ok.
                    });
            }
        };
    }
);