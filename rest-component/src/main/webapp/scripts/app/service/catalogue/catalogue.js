'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('catalogue', {
                parent: 'service',
                url: '/catalogue',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/catalogue/catalogue.html',
                        controller: 'CatalogueController'
                    },
                    'header@': {
                        templateUrl: 'scripts/app/service/catalogue/header_catalogue.html'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('catalogue');
                        $translatePartialLoader.addPart('contact');
                        return $translate.refresh();
                    }]
                }
            });
    });
