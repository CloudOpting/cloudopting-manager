'use strict';

angular.module('cloudoptingApp')
  .directive('bxslider', ['$timeout', function ($timeout) {
    return {
      replace: true,
      restrict: 'E',
      scope: {
        slideit: '='
      },
      template: '<ul class="bxslider hola">' +
                  '<li ng-repeat="slide in slideit" class="slide">' +
                    '<img ng-src="{{slide.src}}" />' +
                  '</li>' +
                '</ul>',
      link: function(scope, element, attrs) {
          $timeout(function () {
            element.bxSlider({
              mode: 'fade',
              auto: true,
              adaptiveHeight: false,
              speed: 50,
              slideSelector : '.slide'
            });
          });
      }
    };
  }]);
