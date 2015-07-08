'use strict';

angular.module('cloudoptingApp')
    .controller('ButtonController', function (SERVICE, $scope, $state, $log, ButtonService) {

        $scope.apiProcessOne = function(){
            ButtonService.apiProcessOne();
        };
    }
);