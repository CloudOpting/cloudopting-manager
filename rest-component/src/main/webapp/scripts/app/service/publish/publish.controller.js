'use strict';

angular.module('cloudoptingApp')
    .controller('PublishController', function (SERVICE, $scope, $state, $log, localStorageService, ApplicationService) {

        /*
         * WIZARD - SCREEN ONE
         */

        $scope.disableUpdate = true;
        $scope.disableSave = false;
        $scope.disableNextOne = true;

        /**
         * Function to save the promotional image.
         * @param images
         */
        $scope.newPromoImage = function(images){
            $scope.files = images;
        };

        function savePromotionalImage() {
            var callback = function(data) {
                //TODO: Update the corresponding file with the corresponding id to keep track.

            };
            //Add content libraries
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            ApplicationService.addPromotionalImage(
                activiti.applicationId,
                activiti.processInstanceId,
                $scope.files,
                callback);
        }

        /**
         * Function to create an application with a 'name', 'description' and 'promoImage'
         * with status 'Draft'
         * FIXME: At the moment it is not used.
         */
        /*
        $scope.saveConfigurationWizardOne = function () {
            var callback = function(activiti){
                localStorageService.set(SERVICE.STORAGE.ACTIVITI, activiti);
                savePromotionalImage();
            };
            var application = {};
            application.applicationName = $scope.name;
            application.applicationDescription=$scope.description;

            //Save the current app in order to use it in the future.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, application);

            //Create the applicaiton.
            ApplicationService.create(application, callback);

            //Move to Step 2 of wizard - Add content library
            $state.go('publish2');
        };
        */
        /**
         * Function to create an application with a 'name', 'description' and 'promoImage'
         * with status 'Draft'
         */
        $scope.saveWizardOne = function() {
            var callback = function(activiti){
                localStorageService.set(SERVICE.STORAGE.ACTIVITI, activiti);
                //FIXME: The processID is only for developmenent.
                $scope.processID = activiti.processInstanceId;
                var app = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);
                app.id = activiti.applicationId;
                localStorageService.set(SERVICE.STORAGE.CURRENT_APP, app);
                savePromotionalImage();
            };
            var application = {};
            application.applicationName = $scope.name;
            application.applicationDescription = $scope.description;
            application.applicationToscaName = $scope.toscaname;

            //Save the current app in order to use it in the future.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, application);

            //Create
            ApplicationService.create(application, callback);
            $scope.disableUpdate = false;
            $scope.disableSave = true;
            $scope.disableNextOne = false;
        };

        $scope.updateWizardOne = function() {
            var callback = function(activiti){
                localStorageService.set(SERVICE.STORAGE.ACTIVITI, activiti);
            };
            var application = {};
            application.applicationName = $scope.name;
            application.applicationDescription = $scope.description;
            application.applicationToscaName = $scope.toscaname;

            //TODO: Fix a bit
            //Create
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            application.id = activiti.applicationId;

            //Save the current app in order to use it in the future.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, application);

            ApplicationService.update(activiti.applicationId, activiti.processInstanceId, application, callback);
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
            var callback = function(data) {
                //TODO: Update the corresponding file with the corresponding id to keep track.

            };
            //Add content libraries
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            ApplicationService.addContentLibrary(
                activiti.applicationId,
                activiti.processInstanceId,
                $scope.libraryList,
                callback);

            /*
             if ($scope.libraryList && $scope.libraryList.length) {
             for (var i = 0; i < $scope.libraryList.length; i++) {
             var file = $scope.libraryList[i];
             $log.info("Filename: " + file.name);
             }
             }
             */

            //Move to Step 3 of wizard - Add TOSCA Archive
            $state.go('publish3');
        };

        /**
         * Function to delete a specific file of the user.
         * @param file
         */
        $scope.deleteLib = function (file){
            console.log("delete " + file.name);
            var index = $scope.libraryList.indexOf(file);
            if (index > -1) {
                $scope.libraryList.splice(index, 1);
            }
            //Send a REST call if it is already persisted in database.
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            ApplicationService.deleteAppFile(activiti.processInstanceId, activiti.applicationId, file);
        };

        /*
         * WIZARD - SCREEN THREE
         */
        $scope.disablePublish = true;
        $scope.toscaFiles = [];

        $scope.addToscaArchive = function(toscaArchive) {
            if(toscaArchive) {
                $scope.toscaFiles.push.apply($scope.toscaFiles, toscaArchive);
            }
        };

        /**
         * Function to send the TOSCA Archive to be saved.
         */
        $scope.saveConfiguration = function () {
            var callback = function(data) {
                //TODO: Show a message of completion.
                $scope.disablePublish = false;
                //TODO: Enable button of publication.

            };
            //Add content libraries
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            //Send the tosca file
            ApplicationService.addToscaArchive(
                activiti.applicationId,
                activiti.processInstanceId,
                $scope.toscaFiles,
                callback);
        };

        /**
         * Function to request the publication of the current application.
         */
        $scope.publishService = function () {
            console.log($scope.contentLib);
            var callback = function (){
                alert("Publication Requested!!!");
            };

            //Request publication
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            var application = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);
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
            console.log("Requesting publication for application:"+angular.toJson(application, true));
            ApplicationService.update(application.id, activiti.processInstanceId, application, callback);
        };

        $scope.isToscaEmpty = function() {
            return $scope.toscaFiles.length==0;
        };
    }
);