'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('form_generation', {
                parent: 'service',
                url: '/form_generation',
                data: {
                    roles: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/form_generation/form_generation.html',
                        controller: 'FormGenerationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('form_generation');
                        return $translate.refresh();
                    }]
                }
            });
    });
