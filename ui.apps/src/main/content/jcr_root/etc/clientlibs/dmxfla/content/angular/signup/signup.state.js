(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing signup component ...");
            $stateProvider
                .state('signup', {
                    url: '/signup',
                    templateUrl: '/content/digital-marketing/en/highmark/fittanylion/signup/jcr:content/sectioncontent.html',
                    controller: 'SignupController'

                });

    }]);
})();
