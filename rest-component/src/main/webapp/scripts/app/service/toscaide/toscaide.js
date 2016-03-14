'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('toscaide', {
                parent: 'service',
                url: '/toscaide',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/toscaide/toscaide.html',
                        controller: 'ToscaideController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('toscaide');
                        return $translate.refresh();
                    }]
                }
            });
    });
