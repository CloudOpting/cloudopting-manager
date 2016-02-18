'use strict';

angular.module('cloudoptingApp')
    .controller('ButtonController', function (SERVICE, $scope, $state, $log, ProcessService) {

        $scope.apiProcessOne = function(){
            var callback = function(data, status, headers, config){
              //Nothing to do
            };
            ProcessService.apiProcessOne(callback);
        };
    }
);