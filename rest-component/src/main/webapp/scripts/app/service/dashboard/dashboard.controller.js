'use strict';

angular.module('cloudoptingApp')
    .controller('DashboardController', function (SERVICE, localStorageService,
                                                 $scope, $state, $log, $location, $filter, $window, $translate,
                                                 Principal, InstanceService, ProcessService, Blob, FileSaver) {

        $scope.currentPage = 0;
        $scope.pageSize = 8;
        $scope.instancesList = [];
        $scope.searchTextInstance = '';
        $scope.numberOfPages = function(){
            return Math.ceil($scope.dataLength()/$scope.pageSize);
        };
        $scope.dataLength = function(){
            return $filter('filter')($scope.instancesList, $scope.searchTextInstance).length;
        };

        $scope.isSubscriber = function(){
            if(Principal.isInRole(SERVICE.ROLE.ADMIN) || Principal.isInRole(SERVICE.ROLE.SUBSCRIBER)){
                return true;
            }
            return false;
        };

        $scope.isPublisher = function(){
            if(Principal.isInRole(SERVICE.ROLE.PUBLISHER)){
                return true;
            }
            return false;
        };

        $scope.toscaide = function(){
            $state.go('toscaide');
        };

        if(Principal.isInRole(SERVICE.ROLE.SUBSCRIBER) || Principal.isInRole(SERVICE.ROLE.ADMIN)) {
            //Get all instances of the user if it is a SUBSCRIBER.
            var callback = function(data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config, null)){
                    //Do something here if all went ok.
                    $scope.instancesList = data;
                }
            };
            InstanceService.findAll(callback);
        }

        $scope.test = function(instance) {
            var callback = function (data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config, "Test requested.")){
                    if(data) {
                        var zip = new Blob([data], {type: 'application/zip'});
                        var fileName = 'TOSCA_Archive_Test.zip';
                        FileSaver.saveAs(zip, fileName);
                    }
                }
            };
            return ProcessService.test(instance.id, callback);
        };

        $scope.demo = function(instance) {
            var callback = function (data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config, "Demo requested.")){
                    //Do something here if all went ok.
                }
            };
            return ProcessService.demo(instance.id, callback);
        };

        $scope.deploy = function(instance) {
            var callback = function (data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config, "Deploy requested.")){
                    localStorageService.set(SERVICE.STORAGE.ACTIVITI.PROCESS_ID, data);
                    $state.go("activiti");
                }
            };
            return ProcessService.deploy(instance.id, callback);
        };

        $scope.stop = function(instance) {
            $scope.errorMessage = "Stop not implemented yet";
            var callback = function(data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config, "Stop requested.")){
                    //Do something here if all went ok.
                }
            };
            //return InstanceService.stop(instance);
        };
        $scope.delete = function(instance) {
            $scope.errorMessage = "Delete not implemented yet";
            var callback = function(data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config, "Delete requested.")){
                    //Do something here if all went ok.
                }
            };
            //return InstanceService.delete(instance.id, callback);
        };

        $scope.monitor = function(instance) {
            if(instance.statusId.status!="Running") {
                $scope.errorMessage = "Cannot check monitoring, the instance is not running.";
            } else {
                localStorageService.set(SERVICE.STORAGE.MONITORING.INSTANCE, instance);
                $state.go('monitoring');
            }
        };

        $scope.start = function(instance) {
            $scope.errorMessage = "Start not implemented yet";
            var callback = function(data, status, headers, config){
                if(checkStatusCallback(data, status, headers, config, "Start requested.")){
                    //Do something here if all went ok.
                }
            };
            //return InstanceService.start(instance);
        };

        //Checks for showing the buttons.
        $scope.showDeploy = function(str){
            return str === "Requested";
        };
        $scope.showStop = function(str){
            return str === "Running";
        };
        $scope.showDelete = function(str){
            return str === "Running";
        };
        $scope.showStart = function(str){
            return str === "Stopped";
        };
        $scope.showMonitor = function(str){
            return str === "Running";
        };

        $scope.errorMessage = null;
        $scope.infoMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config, message){
            if(status==401 || status==403) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = $translate.instant("callback.no_permissions");
                } else {
                    $scope.errorMessage = $translate.instant("callback.session_ended");
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                }
                return false;
            }else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = $translate.instant("callback.generic_error");
                return false;
            } else {
                //Return to the list
                if(message==null){
                    $log.info("Successfully done!");
                } else {
                    $scope.infoMessage = message + $translate.instant("callback.success");
                }
                return true;
            }
        };
    }
);