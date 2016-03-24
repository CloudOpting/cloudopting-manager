'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('catalogue', {
                parent: 'service',
                url: '/catalogue',
                params: {
                    section: null,
                },
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/catalogue/catalogue.html',
                        controller: 'CatalogueController'
                    },
                    'header@': {
                        controller: 'HeaderCatalogueController',
                        templateUrl: 'scripts/app/service/catalogue/header_catalogue.html'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('catalogue');
                        $translatePartialLoader.addPart('contact');
                        $translatePartialLoader.addPart('callback');
                        return $translate.refresh();
                    }]
                }
            });
    });
