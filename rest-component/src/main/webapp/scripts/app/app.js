'use strict';

angular.module('cloudoptingApp', ['LocalStorageModule', 'tmh.dynamicLocale', 'vcRecaptcha', 'ngAutodisable',
    'ngResource', 'ui.router', 'ngCookies', 'pascalprecht.translate', 'ngCacheBuster', 'textAngular',
    'ngFileUpload', 'schemaForm', 'ui.bootstrap', 'checklist-model', 'ngFileSaver', 'duScroll', 'daterangepicker', 'ngAnimate'])

    .run(function ($rootScope, $location, $http, $state, $translate, Auth, Principal, Language, ENV, VERSION) {
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

            // Update the language
            Language.getCurrent().then(function (language) {
                $translate.use(language);
            });
        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;
        });

        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'catalogue'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                //$state.go('home');
                $state.go('catalogue');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };
    })
    
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {

        //enable CSRF
        $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
        $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/, /.*bootstrap.*/, , /.*ui-bootstrap-tpls.*/]);

        $urlRouterProvider.otherwise('/catalogue');
        $stateProvider.state('site', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/components/navbar/navbar.html',
                    controller: 'NavbarController'
                },
                'menu@': {
                    templateUrl: 'scripts/app/menu/menu.html',
                    controller: 'MenuController'
                },
                'footer@': {
                    templateUrl: 'scripts/app/footer/footer.html',
                    controller: 'FooterController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('co_footer');
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('language');
                    $translatePartialLoader.addPart('menu');
                    $translatePartialLoader.addPart('navbar');

                    return $translate.refresh();
                }]
            }
        });

        // Initialize angular-translate
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');
    });

function loadScript(url, callback)
{
    // Adding the script tag to the head as suggested before
    var head = document.getElementsByTagName('head')[0];
    var r_script = head.getElementsByTagName('script');
    for (var index = r_script.length - 1; index >= 0; index--) {
        r_script[index].parentNode.removeChild(r_script[index]);
    }
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;

    // Then bind the event to the callback function.
    // There are several events for cross browser compatibility.
    script.onreadystatechange = callback;
    script.onload = callback;

    // Fire the loading
    head.appendChild(script);

    //modification so we can remove this after
    return script;
}
/*
function vcRecaptchaApiLoaded(){
    grecaptcha.render();
}*/