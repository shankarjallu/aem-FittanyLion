
(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing LoginController component ...");
            $stateProvider
                .state('login', {
                    url: '/login',
     templateUrl: '/content/digital-marketing/en/highmark/fittanylion/login/jcr:content/sectioncontent.html',
                    controller: 'LoginController'

                });

    }]);
})();
