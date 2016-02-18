'use strict';

angular.module('cloudoptingApp')
    .controller('FooterController', function ($state, $scope) {
        $scope.contact = function(){
            $state.go('contact');
        };
    }
);
