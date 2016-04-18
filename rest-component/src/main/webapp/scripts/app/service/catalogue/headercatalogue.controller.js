'use strict';

angular.module('cloudoptingApp')
    .controller('HeaderCatalogueController', function ($scope, $translate, $rootScope) {

        var sliderPath = 'assets/img/slider/';

        function localizedImages() {
            var lang = $translate.use();
            $scope.sliderImages = [
                {src: sliderPath + 'img_slide1_'+lang+'.png' },
                {src: sliderPath + 'img_slide2_'+lang+'.png' },
                {src: sliderPath + 'img_slide3_'+lang+'.png' }
            ];
        }

        localizedImages();
        $rootScope.$on('$translateChangeSuccess', localizedImages);
    }
);
