/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('ApplicationService', function (SERVICE, $http, $log, Upload, $location) {
        var apps = null;
        var app = null;
        var baseURI = 'api/application';

        function upload(idApplication, processID, files, type, callback) {
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    Upload.upload({
                        method: 'POST',
                        url: baseURI + SERVICE.SEPARATOR + idApplication + SERVICE.SEPARATOR + processID + SERVICE.SEPARATOR + 'file',
                        fields: { 'name': file.name, 'type' : type },
                        file: file
                    }).progress(function (evt) {
                        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        $log.debug('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                    }).success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    }).error(function (data, status, headers, config) {
                        $log.error("ApplicationService.upload error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
                }
            }
        }

        return {
            /**
             * Method to get the applications applying filters, and with pagination
             * @returns {*}
             */
            findAll: function (page, size, sortBy, sortOrder, filter, callback) {
                var endpoint = baseURI +
                    '?page=' + page +
                    '&size=' + size +
                    '&sortBy=' + sortBy +
                    '&sortOrder=' + sortOrder +
                    '&filter=' + filter;
                return $http.get(endpoint)
                    .success(function (applications) {
                        apps = applications;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.findAll error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the application list without pagination
             * @returns {*}
             */
            findAllUnpaginated: function (callback) {
                //return $http.get('mocks/applications.js')
                return $http.get(baseURI + SERVICE.SEPARATOR + 'unpaginated')
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.findAllUnpaginated error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            findById: function (id, callback) {
                return $http.get(baseURI + SERVICE.SEPARATOR + id)
                    .success(function (application) {
                        app = application;
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.findById error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            create: function (application, callback) {
                return $http.post(baseURI, application)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.create error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            update: function (idApplication, processId, application, callback) {
                return $http.put(baseURI + SERVICE.SEPARATOR + idApplication + SERVICE.SEPARATOR + processId, application)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.update error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            delete: function (idApplication, callback) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + idApplication)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.delete error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            addPromotionalImage: function (idApplication, processID, files, callback) {
                return upload(idApplication, processID, files, SERVICE.FILE_TYPE.PROMO_IMAGE, callback);
            },
            addContentLibrary: function (idApplication, processID, libraryList, callback) {
                return upload(idApplication, processID, libraryList, SERVICE.FILE_TYPE.CONTENT_LIBRARY, callback);
            },
            addToscaArchive: function (idApplication, processID, toscaFile, callback) {
                return upload(idApplication, processID, toscaFile, SERVICE.FILE_TYPE.TOSCA_ARCHIVE, callback);
            },
            deleteAppFile: function (idApplication, processID, fileId, callback) {
                return $http.delete(baseURI + SERVICE.SEPARATOR + idApplication + SERVICE.SEPARATOR
                    + processID + SERVICE.SEPARATOR + 'file' + SERVICE.SEPARATOR + fileId)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.deleteAppFile error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            }

        }
    }
);