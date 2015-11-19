'use strict';

angular.module('cloudoptingApp')
    .controller('ButtonController', function (SERVICE, $scope, $state, $log, ProcessService) {

        $scope.apiProcessOne = function(){
            ProcessService.apiProcessOne();
        };
    }
);