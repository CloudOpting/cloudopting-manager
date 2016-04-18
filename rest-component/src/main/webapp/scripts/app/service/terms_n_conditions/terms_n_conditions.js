'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('terms_n_conditions', {
                parent: 'service',
                url: '/terms_n_conditions',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/terms_n_conditions/terms_n_conditions.html',
                        controller: 'TermsNConditionsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('terms');
                        return $translate.refresh();
                    }]
                }
            });
    });
