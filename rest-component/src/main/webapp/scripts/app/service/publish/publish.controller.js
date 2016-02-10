'use strict';

angular.module('cloudoptingApp')
    .controller('PublishController', function (SERVICE, $scope, $state, $log, localStorageService, ApplicationService) {

        /*
         * WIZARD - SCREEN ONE
         */

        $scope.disableUpdate = true;
        $scope.disableSave = false;
        $scope.disableNextOne = true;
        $scope.application = {};
        var promoInDatabase = false;

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
            if(promoInDatabase){
                var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);

                var callback = function(data, status, headers, config) {
                    if(checkStatusCallback(data, status, headers, config, "")){
                        promoInDatabase = false;
                    }
                };

                //FIXME: We do not have the file ID
                ApplicationService.deleteAppFile(activiti.processInstanceId, activiti.applicationId, file, callback);
            }

        };

        function savePromotionalImage(activiti) {
            var callback = function(data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config, "")){
                    //TODO: We should update the file id in order to be able to update/delete it
                    promoInDatabase = true;
                }
            };

            ApplicationService.addPromotionalImage(activiti.applicationId, activiti.processInstanceId, $scope.files, callback);
        }

        /**
         * Function to create an application with a 'name', 'description' and 'promoImage'
         * with status 'Draft'
         */
        $scope.saveWizardOne = function() {
            var callback = function(data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config, "")){
                    localStorageService.set(SERVICE.STORAGE.ACTIVITI, data);
                    //TODO: The processID is only for developmenent. Delete once it is done and validated.
                    $scope.processID = data.processInstanceId;

                    //Update the current app with the ID in order to use it in the future.
                    $scope.application.id = data.applicationId;
                    localStorageService.set(SERVICE.STORAGE.CURRENT_APP, $scope.application);

                    savePromotionalImage(data);

                    $scope.disableUpdate = false;
                    $scope.disableSave = true;
                    $scope.disableNextOne = false;
                }
            };

            ApplicationService.create($scope.application, callback);
        };

        $scope.updateWizardOne = function() {
            var callback = function(data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config, "")){
                    localStorageService.set(SERVICE.STORAGE.ACTIVITI, data);
                    //FIXME: We should check if we have to save the promotional images or not. Are they the same? If yes we are going to duplicate it.
                    savePromotionalImage(data);
                }
            };

            ApplicationService.update(activiti.applicationId, activiti.processInstanceId, $scope.application, callback);
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
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);

            var callback = function(data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config, "")){
                    //TODO: We should update the file id in order to be able to update/delete it

                    //Move to Step 3 of wizard - Add TOSCA Archive
                    $state.go('publish3');
                }
            };

            ApplicationService.addContentLibrary(activiti.applicationId, activiti.processInstanceId, $scope.libraryList, callback);

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
                if(checkStatusCallback(data, status, headers, config, "")){
                    promoInDatabase = false;
                }
            };

            //FIXME: Here I do not have the file ID.
            ApplicationService.deleteAppFile(activiti.processInstanceId, activiti.applicationId, file, callback);
            */
        };

        /*
         * WIZARD - SCREEN THREE
         */
        $scope.disablePublish = true;
        $scope.toscaFiles = [];
        var toscaArchiveInDatabase = false;

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
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);

            var callback = function(data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config, "")){
                    //TODO: Show a message of completion.
                    $scope.disablePublish = false;
                    toscaArchiveInDatabase = true;
                    //TODO: Enable button of publication.

                }
            };

            ApplicationService.addToscaArchive(activiti.applicationId, activiti.processInstanceId, $scope.toscaFiles, callback);
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
            if(toscaArchiveInDatabase){
                var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);

                var callback = function(data, status, headers, config) {
                    if(checkStatusCallback(data, status, headers, config, "")){
                        toscaArchiveInDatabase = false;
                    }
                };

                //FIXME: We do not have the file ID
                ApplicationService.deleteAppFile(activiti.processInstanceId, activiti.applicationId, file, callback);
            }

        };

        /**
         * Function to request the publication of the current application.
         */
        $scope.publishService = function () {
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            var application = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);

            var callback = function (data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config, "")){
                    $state.go('publish4');
                }
            };

            requestPublication(activiti, application, callback);
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
            ApplicationService.update(application.id, activiti.processInstanceId, application, callback);
        };

        /*
         * ERROR HANDLING.
         */

        $scope.errorMessage = null;
        $scope.infoMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config, message){
            if(status==401) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = "You have no permissions to do so. Ask for more permissions to the administrator";
                    return false;
                } else {
                    $scope.errorMessage = "Your session has ended. Sign in again. Redirecting to login...";
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                    return false;
                }
            }else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = "An error occurred. Wait a moment and try again, if problem persists contact the administrator";
                return false;
            } else {
                //Return to the list
                $scope.infoMessage = message + " Successfully done!";
                return true;
            }
        };
    }
);