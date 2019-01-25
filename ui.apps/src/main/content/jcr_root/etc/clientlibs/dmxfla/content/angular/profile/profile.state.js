(function(){

    angular.module('fittanyUiApp')
        .config(['$stateProvider', function($stateProvider) {
            console.log("initializing rewards component ...");
                $stateProvider
                    .state('profile', {
                        url: '/profile',
        templateUrl: '/content/digital-marketing/en/highmark/fittanylion/userprofile/jcr:content/section-content.html',
                        controller: 'ProfileController'
                    });
    
        }]);
    })();
