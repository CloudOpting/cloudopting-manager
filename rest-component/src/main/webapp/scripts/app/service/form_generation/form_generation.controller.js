angular.module('cloudoptingApp')
    .controller('FormGenerationController', function (SERVICE, localStorageService, $scope, $state, $log, CustomizationService, Principal, $timeout) {

        if(!Principal.isAuthenticated()){
            $state.go('login');
        }

        var currentApp = localStorageService.get(SERVICE.STORAGE.FORM_GENERATION.APPLICAITON);

        var publishFormCallback = function(data, status, headers, config) {
            checkStatusCallback(data, status, headers, config);
            if($scope.errorMessage==null){
                $scope.schema = data;
            } else {
                $state.go('form_generation');
            }
        };
        CustomizationService.getCustomizationForm(currentApp.id, publishFormCallback);

        $scope.form = [
            "*",
            {
                type: "submit",
                title: "Save"
            }
        ];

        $scope.model = {};


        $scope.onSubmit = function(form) {
            // First we broadcast an event so all fields validate themselves
            $scope.$broadcast('schemaFormValidate');

            // Then we check if the form is valid
            if (form.$valid) {
                // ... do whatever you need to do with your data.
                $log.info("The form is valid, let's send it: " );
                var callback = function(data, status, headers, config) {
                    checkStatusCallback(data, status, headers, config);
                    if(data) {
                        localStorageService.set(SERVICE.STORAGE.CHOOSE_ACCOUNT.INSTANCE, data);
                        $state.go('chooseaccount');
                    }
                };
                CustomizationService.sendCustomizationForm(currentApp.id, $scope.model, callback);
            }
        };

        //Error handling
        $scope.errorMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config){
            if(status==401) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = "You have no permissions to do so. Ask for more permissions to the administrator";
                } else {
                    $scope.errorMessage = "Your session has ended. Sign in again. Redirecting to login...";
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                }
            }else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = "An error occurred. Wait a moment and try again, if problem persists contact the administrator";
            } else {
                $log.info("Service customization succeeded with data: " + data);
            }
        };
    }
);