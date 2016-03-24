'use strict';

angular.module('cloudoptingApp')
    .controller('ContactController', function($scope, $translate, Contact) {

        $scope.infoMessage = null;
        $scope.errorMessage = null;

        $scope.contact = function () {
            $scope.infoMessage = null;
            $scope.errorMessage = null;

            var callback = function (data){
                if(data) {
                    $scope.infoMessage = true;
                } else {
                    $scope.errorMessage = true;
                }
            };

            Contact.save($scope.message, callback);
        };

    }
);
