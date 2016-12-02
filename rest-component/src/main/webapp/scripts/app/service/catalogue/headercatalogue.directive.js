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
                  '<li ng-repeat="slide in slideit track by slide.src" class="slide">' +
                    '<img ng-src="{{slide.src}}" />' +
                  '</li>' +
                '</ul>',
      link: function(scope, element, attrs) {
          var conf = {
              mode: 'fade',
              auto: true,
              adaptiveHeight: false,
              speed: 50,
              slideSelector : '.slide'
          };
          var activateSlider = function() {
              return element.bxSlider(conf);
          };

          $timeout(function () {
              activateSlider();
              scope.$watch('slideit', function() {
                  //activateSlider();
                  element.reloadSlider(conf);
              });
          });



      }
    };
  }]);
