(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
      //  console.log("initializing login component ...");
            $stateProvider
                .state('login', {
                    url: '/login',
                    templateUrl: '/apps/hha/dmxfla/components/content/login/login-template.html',
                    controller: 'LoginController'
                    

                });

    }]);
})();

