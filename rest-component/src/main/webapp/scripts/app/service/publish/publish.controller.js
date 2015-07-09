'use strict';

angular.module('cloudoptingApp')
    .controller('PublishController', function (SERVICE, $scope, $state, $log, localStorageService, ApplicationService) {

        /*
         * WIZARD - SCREEN ONE
         */

        /**
         * Function to save the promotional image.
         * @param images
         */
        $scope.newPromoImage = function(images){
            $scope.files = images;
        };
        /**
         * Function to create an application with a 'name', 'description' and 'promoImage'
         * with status 'Draft'
         */
        $scope.saveConfigurationWizardOne = function () {

            var callback = function(activiti){
                localStorageService.set(SERVICE.STORAGE.ACTIVITI, activiti);
                ApplicationService.addPromotionalImage(activiti.processInstanceId, activiti.applicationId, "promoImage", "descriptionImage", $scope.files);
            };
            var application = {};
            application.applicationName = $scope.name;
            application.applicationDescription=$scope.description;
            //Create
            ApplicationService.create(application, callback);
            //$log.info("Name: " + $scope.name);
            //$log.info("Description: " + $scope.description);
            //if($scope.files) $log.info("Filename: " + $scope.files[0].name);

            //Move to Step 2 of wizard - Add content library
            $state.go('publish2');
        };

        $scope.saveWizardOne = function() {
            var callback = function(activiti){
                localStorageService.set(SERVICE.STORAGE.ACTIVITI, activiti);
                $scope.processID = activiti.processInstanceId;
            };
            var application = {};
            application.applicationName = $scope.name;
            application.applicationDescription=$scope.description;
            //Create
            ApplicationService.create(application, callback);
        };

        $scope.updateWizardOne = function() {
            var callback = function(activiti){
                localStorageService.set(SERVICE.STORAGE.ACTIVITI, activiti);
            };
            var application = {};
            application.applicationName = $scope.name;
            application.applicationDescription=$scope.description;
            //Create
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            ApplicationService.update(activiti.processInstanceId, activiti.applicationId, application, callback);
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
            $scope.libraryName = "hello";
            //Add content libraries
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            ApplicationService.addContentLibrary(activiti.processInstanceId, activiti.applicationId, $scope.libraryList, $scope.libraryName, callback);

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
        $scope.toscaFiles = [];

        /**
         * Function to send the TOSCA Archive to be saved.
         */
        $scope.saveConfiguration = function () {
            //Send the tosca file

            //FIXME: Not yet implemented.
            //ApplicationService.addToscaFile($scope.toscaFiles[0], $scope.idApplication);
        };

        /**
         * Function to request the publication of the current application.
         */
        $scope.publishService = function () {
            console.log($scope.contentLib);

            //Request publication
            var activiti = localStorageService.get(SERVICE.STORAGE.ACTIVITI);
            ApplicationService.requestPublication(activiti.processInstanceId, activiti.applicationId);
        };
    }
);