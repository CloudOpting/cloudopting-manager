/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
    .factory('ApplicationService', function (SERVICE, $http, $log, Upload) {

        var baseURI = 'api/application';
        var baseURISize = 'api/applicationSize';

        /**
         * Method to upload a file for the application with identification 'applicationId'.
         * @param applicationId Identification of the application.
         * @param processID Process id of the current publication flow.
         * @param file File to be uploaded.
         * @param type Type of the file that will be uploaded.
         * @param callback Function that will take care of the returned objects.
         */
        function upload(applicationId, processID, file, type, isZipFile, callback) {
            var endpoint = baseURI +
                SERVICE.SEPARATOR +
                applicationId +
                SERVICE.SEPARATOR +
                processID +
                SERVICE.SEPARATOR +
                'file';
            Upload.upload({
                method: 'POST',
                url: endpoint,
                fields: { 'name': file.name, 'type' : type, 'isZipFile' : isZipFile },
                file: file
            }).progress(function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                $log.debug('progress: ' + progressPercentage + '% ' + evt.config.file.name);
            }).success(function (data, status, headers, config) {
                callback(data, status, headers, config);
            }).error(function (data, status, headers, config) {
                $log.error("ApplicationService.upload error. Type: " + type +
                    ", data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                callback(data, status, headers, config);
            });
        }

        return {

            /**
             * Method to get the applications applying filters, and with pagination.
             * @param page Number of the page to be returned.
             * @param size Size of the page.
             * @param sortBy Parameter of the application that will be used to sort the list.
             * @param sortOrder It can be ascendant 'asc' or descendant 'desc'.
             * @param filter Word that will be used in any field of application to filter.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findAll: function (page, size, sortBy, sortOrder, filter, callback) {
                var endpoint = baseURI +
                    '?page=' + page +
                    '&size=' + size;
                if(sortBy != null && sortBy != undefined) { endpoint += '&sortBy=' + sortBy; }
                if(sortOrder != null && sortOrder != undefined) { endpoint += '&sortOrder=' + sortOrder; }
                if(filter != null && filter != undefined) { endpoint += '&filter=' + filter; }
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.findAll error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * FIXME: DELETE THIS METHOD
             * Method to get the application list without pagination.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findAllUnpaginated: function (callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    'unpaginated';
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.findAllUnpaginated error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get an application with identification 'applicationId'.
             * @param applicationId Identification of the application.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findById: function (applicationId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId;
                return $http.get(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.findById error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to create an application.
             * @param application New application to be created.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
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

            /**
             * Method to update an application with identification 'applicationId'.
             * @param applicationId Identification of the application.
             * @param processId Process id of the current publication flow.
             * @param application Application with the fields to be modified.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            update: function (applicationId, processId, application, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId +
                    SERVICE.SEPARATOR +
                    processId;
                return $http.put(endpoint, application)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.update error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to delete an application with identification 'applicationId'.
             * @param applicationId Identification of the application.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            delete: function (applicationId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId;
                return $http.delete(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.delete error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to upload a promotional image for the application with identification 'applicationId'.
             * For more information check @upload method.
             * @param applicationId Identification of the application.
             * @param processID Process id of the current publication flow.
             * @param file File to be uploaded.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            addPromotionalImage: function (applicationId, processID, file, callback) {
                return upload(applicationId, processID, file, SERVICE.FILE_TYPE.PROMO_IMAGE, false, callback);
            },

            /**
             * Method to upload a content library file for the application with identification 'applicationId'.
             * For more information check @upload method.
             * @param applicationId Identification of the application.
             * @param processID Process id of the current publication flow.
             * @param file File to be uploaded.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            addContentLibrary: function (applicationId, processID, file, isZipFile, callback) {
                return upload(applicationId, processID, file, SERVICE.FILE_TYPE.CONTENT_LIBRARY, isZipFile, callback);
            },

            /**
             * Method to upload a tosca archive for the application with identification 'applicationId'.
             * For more information check @upload method.
             * @param applicationId Identification of the application.
             * @param processID Process id of the current publication flow.
             * @param file File to be uploaded.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            addToscaArchive: function (applicationId, processID, file, callback) {
                return upload(applicationId, processID, file, SERVICE.FILE_TYPE.TOSCA_ARCHIVE, false, callback);
            },

            /**
             * Method to delete a file from an application with identification 'applicationId'.
             * @param applicationId Identification of the application.
             * @param processID Process id of the current publication flow.
             * @param fileId Id of the file that has to be deleted.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            deleteAppFile: function (applicationId, processID, fileId, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId +
                    SERVICE.SEPARATOR +
                    processID +
                    SERVICE.SEPARATOR +
                    'file' +
                    SERVICE.SEPARATOR +
                    fileId;
                return $http.delete(endpoint)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.deleteAppFile error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            /**
             * Method to get all the possible sizes of an application.
             * @param callback Function that will take care of the returned objects.
             * @returns {*}
             */
            findAllSizes: function (callback) {
                return $http.get(baseURISize)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.findAllSizes error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },

            updateLogo: function (applicationId, file, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId +
                    SERVICE.SEPARATOR +
                    'updatelogo';
                return Upload.upload({
                    method: 'POST',
                    url: endpoint,
                    fields: { 'name': file.name, 'type' : SERVICE.FILE_TYPE.PROMO_IMAGE },
                    file: file
                }).progress(function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    $log.debug('ApplicationService.updateLogo progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function (data, status, headers, config) {
                    callback(data, status, headers, config);
                }).error(function (data, status, headers, config) {
                    $log.error("ApplicationService.updateLogo error. Data: " + data +
                        ", status: " + status + ", headers: " + headers + ", config: " + config);
                    callback(data, status, headers, config);
                });
            },

            updateMetadata: function(applicationId, application, callback){
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId +
                    SERVICE.SEPARATOR +
                    'updatemetadata';
                return $http.put(endpoint, application)
                    .success(function (data, status, headers, config) {
                        callback(data, status, headers, config);
                    })
                    .error(function (data, status, headers, config) {
                        $log.error("ApplicationService.update error. " +
                            "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
                        callback(data, status, headers, config);
                    });
            },
            addMediaFile: function(applicationId, file, isZipFile, callback){
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId +
                    SERVICE.SEPARATOR +
                    'mediafile';
                return Upload.upload({
                    method: 'POST',
                    url: endpoint,
                    fields: { 'name': file.name, 'type' : SERVICE.FILE_TYPE.CONTENT_LIBRARY, 'isZipFile' :  isZipFile },
                    file: file
                }).progress(function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    $log.debug('ApplicationService.addMediaFile progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function (data, status, headers, config) {
                    callback(data, status, headers, config);
                }).error(function (data, status, headers, config) {
                    $log.error("ApplicationService.addMediaFile error. Data: " + data +
                        ", status: " + status + ", headers: " + headers + ", config: " + config);
                    callback(data, status, headers, config);
                });
            },

            updateToscaFile: function (applicationId, file, callback) {
                var endpoint = baseURI +
                    SERVICE.SEPARATOR +
                    applicationId +
                    SERVICE.SEPARATOR +
                    'updatetoscafile';
                return Upload.upload({
                    method: 'POST',
                    url: endpoint,
                    fields: { 'name': file.name, 'type' : SERVICE.FILE_TYPE.TOSCA_ARCHIVE },
                    file: file
                }).progress(function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    $log.debug('ApplicationService.updateToscaFile progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function (data, status, headers, config) {
                    callback(data, status, headers, config);
                }).error(function (data, status, headers, config) {
                    $log.error("ApplicationService.updateToscaFile error. Data: " + data +
                        ", status: " + status + ", headers: " + headers + ", config: " + config);
                    callback(data, status, headers, config);
                });
            },
        }
    }
);