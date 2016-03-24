'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('chooseaccount', {
                parent: 'service',
                url: '/chooseaccount',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/chooseaccount/chooseaccount.html',
                        controller: 'ChooseAccountController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('chooseaccount');
                        $translatePartialLoader.addPart('callback');
                        return $translate.refresh();
                    }]
                }
            });
    });
