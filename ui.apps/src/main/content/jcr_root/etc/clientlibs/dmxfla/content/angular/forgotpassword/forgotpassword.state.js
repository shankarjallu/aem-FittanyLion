(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing signup component ...");
            $stateProvider
                .state('forgotpassword', {
                    url: '/forgotpassword',
                    templateUrl: '/content/digital-marketing/en/highmark/fittanylion/forgotpassword.html',
                    controller: 'ForgotPassword'

                });

    }]);
})();
