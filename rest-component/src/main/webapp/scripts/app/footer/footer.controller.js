'use strict';

angular.module('cloudoptingApp')
    .controller('FooterController', function (SERVICE, $state, $scope) {

        $scope.contact = function(){
            $state.go('contact');
        };
    }
);
