'use strict';


angular.module('cloudoptingApp')
    .controller('MenuController', function (SERVICE,
                                            $state, $scope, $document, $rootScope, $window, $translate, $timeout, $cookieStore,
                                            Principal, Auth) {

        $scope.logoutButton = Principal.isAuthenticated();
        //$scope.name = Principal.isAuthenticated ? Principal.identity().login : '';
        if (Principal.isAuthenticated()) {
            Principal.identity().then(function (account) {
                $scope.name = account.login;
            });
        }

        $scope.logout = function(){
            Auth.logout();
            $state.go("login");
        };

        $scope.profile = function(){
            $state.go("profile");
        };

        $scope.dashboard = function(){
            $state.go("dashboard");
        };

        $scope.scrollTo = function(element) {
            $( 'html, body').animate({
                scrollTop: $(element).offset().top
            }, 500);
        };

        $scope.contact = function() {
            if($state.current.name == "catalogue"){
                var someElement = angular.element(document.getElementById('contact'));
                //$document.scrollToElementAnimated( someElement, 30, 500 );
                $scope.scrollTo( "#contact");
            } else {
                $state.go('catalogue', { section: "contact" }, {reload: true} );
            }
        };
        $scope.catalogue = function() {
            if($state.current.name == "catalogue"){
                var someElement = angular.element(document.getElementById('services'));
                //$document.scrollToElementAnimated( someElement, 30, 500 );
                $scope.scrollTo( "#services");
            } else {
                $state.go('catalogue');
            }
        };

        $scope.$watch(
            function() {
                return Principal.isAuthenticated();
            },
            function(newVal, oldVal)
            {
                $scope.logoutButton = Principal.isAuthenticated();
                //$scope.name = Principal.isAuthenticated ? Principal.identity().login : '';
                if (Principal.isAuthenticated()) {
                    Principal.identity().then(function (account) {
                        $scope.name = account.login;
                    });
                }

                //TODO: Wrong. Check uses and delete it.
                $scope.isPublisher = function (){
                    return Principal.isInRole(SERVICE.ROLE.SUBSCRIBER);
                };

                $scope.logout = function(){
                    Auth.logout();
                    $state.go("catalogue");
                };
            },
            true
        );

        $scope.showMenu = function(item){
            if(Principal.isInRole(SERVICE.ROLE.ADMIN)){
                if(item=='dashboard') return false;
                return true;
            }
            else if(Principal.isInRole(SERVICE.ROLE.OPERATOR)){
                if(item=='dashboard' || item=='drop_down' || item=='publish' || item=='list' || item=='user_manager' || item=='org_manager' ) {
                    return true;
                }
            }
            else if(Principal.isInRole(SERVICE.ROLE.PUBLISHER)){
                if(item=='dashboard' || item=='drop_down' || item=='publish' || item=='list' || item=='toscaide') {
                    return true;
                }
            }
            else if(Principal.isInRole(SERVICE.ROLE.SUBSCRIBER)){
                if(item=='dashboard') return true;
                return false;
            }
        };



/*
CAPTCHA ACCORDING TO THE CURRENT LANGUAGE
 */

        $scope.loadGoogleRecaptcha = function (id, lang) {
            /// using timeout not to use $apply
            $timeout(function () {
                //set default value
                var elemntID = id || false;
                var language = lang || 'en';

                if (!elemntID) {
                    console.warn('NO Id selected for re-captcha loading!')
                    return false;
                }

                var element = document.getElementById(elemntID)
                if (angular.element(element).length == 0) {
                    console.warn('provided id doesn\'t exist ', elemntID);
                    return false;
                }

                //Delete oldscript
                if ($rootScope.addedScript)
                    angular.element($rootScope.addedScript).remove();

                //clear current recaptcha container
                angular.element(element).empty();

                var url = "https://www.google.com/recaptcha/api.js?onload=vcRecaptchaApiLoaded&render=explicit&hl=" + language;

                return $window.loadScript(url, function () {
                    console.log('Scipt loaded:', url);
                })

            });

        };

        $rootScope.addedScript = $scope.loadGoogleRecaptcha('multilingual_recaptcha' , $cookieStore.get( 'NG_TRANSLATE_LANG_KEY'));
/*
        $rootScope.changeLanguage = function (langKey) {

            $translate.use(langKey);
            $rootScope.selectedLanguage = langKey;
            $scope.loadGoogleRecaptcha('multilingual_recaptcha' , langKey);
        };
        */
/*
        $scope.$watch(
            'selectedLanguage', function (n, o) {
                //putting if to prevent double fetch the first time
                if (!!n && n != o){
                    $rootScope.addedScript = $scope.loadGoogleRecaptcha('multilingual_recaptcha', n);
                }
            }
        );

*/
    }
);
