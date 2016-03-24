'use strict';

angular.module('cloudoptingApp')
    .controller('PrivacyPolicyController', function ($scope) {

        $scope.scrollTo = function(element) {
            $( 'html, body').animate({
                scrollTop: $(element).offset().top
            }, 500);
        };
        $scope.scrollTo( "#page-top");
    }
);