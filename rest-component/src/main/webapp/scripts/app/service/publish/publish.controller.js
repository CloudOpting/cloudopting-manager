'use strict';

angular.module('cloudoptingApp')
    .controller('PublishController', function ($scope, $state, $log, ApplicationService) {

        /*
         * WIZARD - SCREEN ONE
         */
        /**
         * Function to create an application with a 'name', 'description' and 'promoImage'
         * with status 'Draft'
         */
        $scope.saveConfigurationWizardOne = function () {

            var updateApplicationId = function(applicationId){
                $scope.idApplication = applicationId;
            };

            //Create
            ApplicationService.create($scope.name, $scope.description, $scope.files, updateApplicationId);
            //$log.info("Name: " + $scope.name);
            //$log.info("Description: " + $scope.description);
            //if($scope.files) $log.info("Filename: " + $scope.files[0].name);

            //Move to Step 2 of wizard - Add content library
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
        $scope.$watch(
            function() {
                return $scope.contentLib;
            },
            function(newVal, oldVal)
            {
                if($scope.contentLib) {
                    $scope.libraryList.push.apply($scope.libraryList, $scope.contentLib);
                }
            },
            true
        );

        /**
         * Function to save the content files added by the user
         */
        $scope.saveConfigurationWizardTwo = function () {
            var updateLibraryId = function(data) {
                //TODO: Update the corresponding file with the correspongind id to keep track.

            };
            //Add content libraries
            ApplicationService.addContentLibrary($scope.libraryList, $scope.idApplication, $scope.libraryName, updateLibraryId);

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
            ApplicationService.deleteContentLibrary($scope.idApplication, file);
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
            ApplicationService.requestPublication($scope.idApplication);
        };
    }
);