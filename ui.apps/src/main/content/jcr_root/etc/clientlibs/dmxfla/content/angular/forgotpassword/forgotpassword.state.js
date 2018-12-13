(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing signup component ...");
            $stateProvider
                .state('forgotpassword', {
                    url: '/forgotpassword',
                    templateUrl: '/apps/hha/dmxfla/components/content/forgotpassword/forgotpassword.html',
                    controller: 'ForgotPassword'

                });

    }]);
})();
