'use strict';

angular.module('cloudoptingApp')
    .controller('PublishController', function (SERVICE, localStorageService,
                                               $scope, $state, $log, $translate,
                                               Principal, ApplicationService) {

        //If it is a modification we have to prepare everything to be edited.
        var isEdition = localStorageService.get(SERVICE.STORAGE.PUBLISH.IS_EDITION);
        if(isEdition=="true"){
            //Get the app to be modified.
            $scope.application = localStorageService.get(SERVICE.STORAGE.PUBLISH.APPLICATION);
            $scope.disableUpdate = false;
            $scope.disableSave = true;
            $scope.disableNextOne = false;
            $scope.disableNextTwo = false;
            var promoInDatabase = true;

            if($scope.application!= null && $scope.application!=undefined){
                //Prepare the name of the file
                if($scope.application.applicationLogoReference) {
                    var tokensFile = $scope.application.applicationLogoReference.split('/');
                    var name = tokensFile[tokensFile.length - 1];
                    $scope.files = [];
                    $scope.files.push({name: name});
                }
                //Prepare the name of the tosca
                if($scope.application.applicationToscaTemplate) {
                    var tokensFile = $scope.application.applicationToscaTemplate.split('/');
                    var name = tokensFile[tokensFile.length - 1];
                    $scope.toscaFiles = [];
                    $scope.toscaFiles.push({name: name});
                    var toscaArchiveInDatabase = true;

                }
            }
        } else {
            //If it is a new service we start with diferent parameters.
            $scope.application = {};
            //Set the default text to the application description.
            $scope.application.applicationDescription=$translate.instant("publish.description.template");
            $scope.disableUpdate = true;
            $scope.disableSave = false;
            $scope.disableNextOne = true;
            $scope.disableNextTwo = true;
            var promoInDatabase = false;
            //Prepare tosca
            $scope.toscaFiles = [];
            var toscaArchiveInDatabase = false;
        }

        /*
         * WIZARD - SCREEN ONE
         */

        //Find all sizes for the combo.
        var callbackSizes = function(data, status, headers, config) {
            if(checkStatusCallback(data, status, headers, config)){
                $scope.applicationSizes = data;
            }
        };

        ApplicationService.findAllSizes(callbackSizes);
        /**
         * Function to save the promotional image into the $scope list
         * @param images
         */
        $scope.newPromoImage = function(images){
            $scope.files = images;
        };

        /**
         * Function to delete the promotional image form the $scope and database list.
         * @param file
         */
        $scope.deletePromotionalImage = function (file){
            //Delete from the $scope
            var index = $scope.files.indexOf(file);
            if (index > -1) {
                $scope.files.splice(index, 1);
                if($scope.files.length == 0) {
                    $scope.files = null;
                }
            }

            //If already saved into database we have to delete it from there also.
            if(promoInDatabase && isEdition!="true"){
                var activiti = localStorageService.get(SERVICE.STORAGE.PUBLISH.ACTIVITI);

                var callback = function(data, status, headers, config) {
                    if(checkStatusCallback(data, status, headers, config)){
                        localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, data);
                        promoInDatabase = false;
                    }
                };

                //FIXME: We do not have the file ID
                return ApplicationService.deleteAppFile(activiti.processInstanceId, activiti.applicationId, activiti.jrPath, callback);
            }

            if(isEdition=="true") {
                $scope.disableNextOne = true;
            }
        };

        function savePromotionalImage(activiti) {
            if ($scope.files && $scope.files.length) {
                for (var i = 0; i < $scope.files.length; i++) {
                    var file = $scope.files[i];
                    var callback = function(data, status, headers, config) {
                        if(checkStatusCallback(data, status, headers, config)){
                            localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, data);
                            promoInDatabase = true;
                            $scope.disableNextOne = false;
                        }
                    };

                    ApplicationService.addPromotionalImage(activiti.applicationId, activiti.processInstanceId, file, callback);
                }
            }
        }

        function updatePromotionalImage(applicationId) {
            if ($scope.files && $scope.files.length) {
                for (var i = 0; i < $scope.files.length; i++) {
                    var file = $scope.files[i];
                    if(file.lastModified!=null && file.lastModified!=undefined) {
                        var callback = function (data, status, headers, config) {
                            if (checkStatusCallback(data, status, headers, config)) {
                                promoInDatabase = true;
                                $scope.disableNextOne = false;
                            }
                        };


                        ApplicationService.updateLogo(applicationId, file, callback);
                    }
                }
            }
        }

        /**
         * Function to create an application with a 'name', 'description' and 'promoImage'
         * with status 'Draft'
         */
        $scope.saveWizardOne = function() {
            var callback = function(data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config)){
                    localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, data);
                    //TODO: The processID is only for developmenent. Delete once it is done and validated.
                    $scope.processID = data.processInstanceId;

                    //Update the current app with the ID in order to use it in the future.
                    $scope.application.id = data.applicationId;
                    localStorageService.set(SERVICE.STORAGE.PUBLISH.APPLICATION, $scope.application);

                    savePromotionalImage(data);

                    $scope.disableUpdate = false;
                    $scope.disableSave = true;
                    $scope.disableNextOne = false;
                }
            };

            return ApplicationService.create($scope.application, callback);
        };

        $scope.updateWizardOne = function() {
            var applicationId = $scope.application.id;
            var callback = function(data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config)){
                    localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, data);

                    //TODO: UPDATE PROMOTIONAL IMAGE
                    updatePromotionalImage(applicationId);

                }
            };

            if(isEdition=="true"){
                //TODO: UPDATE INFORMATION WITHOUT
                //TODO: GET THE applicationID
                return ApplicationService.updateMetadata(applicationId, $scope.application, callback);
            } else {
                var activiti = localStorageService.get(SERVICE.STORAGE.PUBLISH.ACTIVITI);
                return ApplicationService.update(activiti.applicationId, activiti.processInstanceId, $scope.application, callback);
            }
        };

        $scope.nextWizardOne = function() {
            $state.go('publish2');
        };

        /*
         * WIZARD - SCREEN TWO
         */
        $scope.libraryList = [];
        $scope.contentLib = null;


        $scope.isLibraryEmpty = function() {
            return $scope.libraryList.length==0;
        };

        /**
         * Watch the contentLib to refresh the internal list of files that the user wants to upload.
         */
        /*
        $scope.$watch(function(){
            return $scope.contentLib;
        }, function() {
                if($scope.contentLib) {
                    $scope.libraryList.push.apply($scope.libraryList, $scope.contentLib);
                }
            }
        );*/
        $scope.nextWizardTwo = function() {
            $state.go('publish3');
        };
        $scope.skipWizardTwo = function() {
            //FIXME: do we have to do something with activity in order to skip this step?
            $state.go('publish3');
        }

        /**
         * Function to save the content libraries into an array.
         * @param contentLib
         */
        $scope.newContentLib = function(contentLib){
            if(contentLib) {
                $scope.libraryList.push.apply($scope.libraryList, contentLib);
            }
        };

        /**
         * Function to save the content files added by the user
         */
        $scope.saveConfigurationWizardTwo = function () {
            if(isEdition=="true"){
                if ($scope.libraryList && $scope.libraryList.length) {
                    for (var i = 0; i < $scope.libraryList.length; i++) {
                        var file = $scope.libraryList[i];
                        if(file.lastModified!=null && file.lastModified!=undefined) {
                            var callback = function (data, status, headers, config) {
                                if (checkStatusCallback(data, status, headers, config)) {
                                    $scope.disableNextTwo=false;
                                }
                            };
                            ApplicationService.addMediaFile($scope.application.id, file, callback);
                        }
                    }
                }
            } else {
                var activiti = localStorageService.get(SERVICE.STORAGE.PUBLISH.ACTIVITI);

                if ($scope.libraryList && $scope.libraryList.length) {
                    for (var i = 0; i < $scope.libraryList.length; i++) {
                        var file = $scope.libraryList[i];

                        var callback = function (data, status, headers, config) {
                            if (checkStatusCallback(data, status, headers, config)) {
                                localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, data);
                                $scope.disableNextTwo=false;
                            }
                        };

                        ApplicationService.addContentLibrary(activiti.applicationId, activiti.processInstanceId, file, callback);
                    }
                }
            }
            return;

            /*
             if ($scope.libraryList && $scope.libraryList.length) {
             for (var i = 0; i < $scope.libraryList.length; i++) {
             var file = $scope.libraryList[i];
             $log.info("Filename: " + file.name);
             }
             }
             */
        };

        /**
         * Function to delete a specific file of the user.
         * @param file
         */
        $scope.deleteLib = function (file){
            //Delete from the $scope
            var index = $scope.libraryList.indexOf(file);
            if (index > -1) {
                $scope.libraryList.splice(index, 1);
            }

            //We do not need to delete from database. They are not saved yet.
            /*
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);

            var callback = function(data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config)){
                    localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, data);
                    promoInDatabase = false;
                }
            };

            //FIXME: Here I do not have the file ID.
            ApplicationService.deleteAppFile(activiti.processInstanceId, activiti.applicationId, activiti.jrPath, callback);
            */
        };

        /*
         * WIZARD - SCREEN THREE
         */
        $scope.disablePublish = true;
        $scope.disablePublishEdition = false;

        $scope.isToscaFilesEmpty = function() {
            return $scope.toscaFiles.length==0;
        };

        $scope.addToscaArchive = function(toscaArchive) {
            if(toscaArchive) {
                $scope.toscaFiles = toscaArchive;
            }
        };

        /**
         * Function to send the TOSCA Archive to be saved.
         */
        $scope.saveConfiguration = function () {
            if(isEdition=="true"){
                if ($scope.toscaFiles && $scope.toscaFiles.length) {
                    for (var i = 0; i < $scope.toscaFiles.length; i++) {
                        var file = $scope.toscaFiles[i];
                        if(file.lastModified!=null && file.lastModified!=undefined) {
                            var callback = function (data, status, headers, config) {
                                if (checkStatusCallback(data, status, headers, config)) {
                                    toscaArchiveInDatabase = true;
                                    $scope.disablePublishEdition = false;
                                }
                            };
                            ApplicationService.updateToscaFile($scope.application.id, file, callback);
                        }
                    }
                }
            } else {
                var activiti = localStorageService.get(SERVICE.STORAGE.PUBLISH.ACTIVITI);

                if ($scope.toscaFiles && $scope.toscaFiles.length) {
                    for (var i = 0; i < $scope.toscaFiles.length; i++) {
                        var file = $scope.toscaFiles[i];

                        var callback = function (data, status, headers, config) {
                            if (checkStatusCallback(data, status, headers, config)) {
                                localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, data);
                                $scope.disablePublish = false;
                                toscaArchiveInDatabase = true;
                            }
                        };
                        ApplicationService.addToscaArchive(activiti.applicationId, activiti.processInstanceId, file, callback);
                    }
                }

            }
            return;
        };

        /**
         * Function to delete the tosca archive.
         * @param file
         */
        $scope.deleteToscaArchive = function (file){
            //Delete from $scope list
            var index = $scope.toscaFiles.indexOf(file);
            if (index > -1) {
                $scope.toscaFiles.splice(index, 1);
            }

            //If already saved into database we have to delete it from there also.
            if(toscaArchiveInDatabase && isEdition!="true"){
                var activiti = localStorageService.get(SERVICE.STORAGE.PUBLISH.ACTIVITI);

                var callback = function(data, status, headers, config) {
                    if(checkStatusCallback(data, status, headers, config)){
                        localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, data);
                        toscaArchiveInDatabase = false;
                    }
                };

                return ApplicationService.deleteAppFile(activiti.processInstanceId, activiti.applicationId, activiti.jrPath, callback);
            }

            if(isEdition=="true") {
                $scope.disablePublishEdition = true;
            }

        };

        /**
         * Function to request the publication of the current application.
         */
        $scope.publishService = function () {
            var activiti = localStorageService.get(SERVICE.STORAGE.PUBLISH.ACTIVITI);
            var application = localStorageService.get(SERVICE.STORAGE.PUBLISH.APPLICATION);

            var callback = function (data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config)){
                    localStorageService.set(SERVICE.STORAGE.PUBLISH.APPLICATION, null);
                    localStorageService.set(SERVICE.STORAGE.PUBLISH.ACTIVITI, null);
                    $state.go('publish4');
                }
            };

            return requestPublication(activiti, application, callback);
        };

        $scope.finish = function () {
            $state.go('publish4');
        };

        /**
         * Method to get the application parameters to be customized.
         *
         * @returns {*}
         */
        var requestPublication = function (activiti, application, callback) {
            //activiti.processInstanceId, activiti.applicationId
            application.status = "Requested";
            $log.debug("Requesting publication for application: " + angular.toJson(application, true));
            return ApplicationService.update(application.id, activiti.processInstanceId, application, callback);
        };

        //////////
        // ERROR HANDLING.
        //////////

        $scope.errorMessage = null;
        $scope.infoMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config){
            if(status==401 || status==403) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = $translate.instant("callback.no_permissions");
                    return false;
                } else {
                    $scope.errorMessage = $translate.instant("callback.session_ended");
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                    return false;
                }
            }else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = $translate.instant("callback.generic_error");
                return false;
            } else {
                //Return to the list
                $scope.infoMessage = $translate.instant("callback.success");
                return true;
            }
        };
    }
);
