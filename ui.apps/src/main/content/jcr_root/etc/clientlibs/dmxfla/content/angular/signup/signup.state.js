(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing signup component ...");
            $stateProvider
                .state('signup', {
                    url: '/signup',
                    templateUrl: '/apps/hha/dmxfla/components/content/signup/signup-template.html',
                    controller: 'SignupController'

                });

    }]);
})();
