angular.module('cloudoptingApp')
    .controller('FormGenerationController', function (SERVICE, localStorageService, $scope, $log, CustomizationService) {

        /*
    	$http.get("/api/application/1/getCustomizationForm").success(function(res){
    		console.log(res);
    		$scope.schema = res;});
        $scope.schema = {
            type: "object",
            properties: {
                name: { type: "string", minLength: 2, title: "Name", description: "Name or alias" },
                title: {
                    type: "string",
                    enum: ['dr','jr','sir','mrs','mr','NaN','dj']
                }
            }
        };
*/
        var currentApp = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);

        var callback = function(data) {
            $log.info(data);
            $scope.schema = data;
        };

        var idApp = currentApp.id;
        //FIXME: Delete hardcoded value.
        idApp = 1;
        CustomizationService.getCustomizationForm(idApp, callback);

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
                $log.info("The form is valid, let's send it: " + form.node_id.$modelValue + " " +  form.memory.$modelValue);
                var callback = function(data) {
                    $log.info("sendCustomForm succeeded with data: " + data);
                };
                CustomizationService.sendCustomizationForm(form, callback);
            }
        }
    }
);